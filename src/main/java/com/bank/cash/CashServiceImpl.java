package com.bank.cash;

import com.bank.atm.AtmResponseDTO;
import com.bank.exceptions.AmountCanNotBeWithdrawnException;
import com.bank.helpers.ConversionHelpers;
import com.bank.notifications.CashNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.bank.helpers.ConversionHelpers.cashDtoListToCashList;
import static com.bank.helpers.ConversionHelpers.cashListToCashDtoList;

@Service
@Slf4j
public class CashServiceImpl implements CashService {

    private CashRepository cashRepository;


    @Autowired
    private CashNotificationService cashNotificationService;


    @Autowired
    public CashServiceImpl(CashRepository cashRepository) {
        this.cashRepository = cashRepository;
    }

    @Override
    public List<CashDTO> checkIfWeHaveAmount(int amount) throws AmountCanNotBeWithdrawnException {
        log.info("Check if amount: " + amount + " is available in ATM");

        List<CashDTO> resultCashList = new ArrayList<>();
        List<Cash> allCash = cashRepository.findAll();
        AtomicLong totalAtmAmountAtomic = new AtomicLong();
        List<CashDTO> cashDTOList = cashListToCashDtoList(allCash);

        cashDTOList.forEach(cash -> totalAtmAmountAtomic.addAndGet(cash.getBillValue() * cash.getNumberOfAvailableBills()));
        long totalAtmAmount = totalAtmAmountAtomic.get();


        if (totalAtmAmount > amount) {
            log.info("Total ATM Amount:" + totalAtmAmount);

            Collections.sort(cashDTOList);
            Collections.reverse(cashDTOList);

            getExactCashNumberList(cashDTOList, resultCashList, amount);

            log.info("Cash that will be taken out is: " + resultCashList);


            return resultCashList;
        } else {
            log.error("Not enough cash in the ATM to withdraw!");

            return null;
        }
    }

    private void getExactCashNumberList(List<CashDTO> availableAmount, List<CashDTO> partialResultList, int withdrawAmount) throws AmountCanNotBeWithdrawnException {
        int requiredNrOfBills;
        int billValueToProcess = availableAmount.get(0).getBillValue();
        long numberOfAvailableBills = availableAmount.get(0).getNumberOfAvailableBills();
        if (withdrawAmount % billValueToProcess == 0) {
            requiredNrOfBills = withdrawAmount / billValueToProcess;

            if (requiredNrOfBills > numberOfAvailableBills) {
                throw new AmountCanNotBeWithdrawnException("Amount cannot be withdrawn!\n" +
                        "Not Enough bills for that amount!");
            } else {
                partialResultList.add(CashDTO.builder()
                        .billValue(billValueToProcess)
                        .numberOfAvailableBills(requiredNrOfBills)
                        .build());
            }

        } else if (availableAmount.stream().anyMatch(cashDTO -> withdrawAmount % cashDTO.getBillValue() == 0)) {
            int newWithdrawAmount;

            if (withdrawAmount > billValueToProcess) {
                requiredNrOfBills = withdrawAmount / billValueToProcess;

                if (requiredNrOfBills > numberOfAvailableBills) {
                    throw new AmountCanNotBeWithdrawnException("Amount cannot be withdrawn!\n" +
                            "Not Enough bills for that amount!");
                } else {
                    newWithdrawAmount = withdrawAmount % billValueToProcess;
                    partialResultList.add(CashDTO.builder()
                            .billValue(billValueToProcess)
                            .numberOfAvailableBills(requiredNrOfBills)
                            .build());
                }
            } else {
                newWithdrawAmount = withdrawAmount;
            }

            availableAmount.remove(0);

            getExactCashNumberList(availableAmount, partialResultList, newWithdrawAmount);


        } else {
            Collections.sort(availableAmount);
            log.error("CURRENT AMOUNT CANNOT BE WITHDRAWN BECAUSE THERE ARE NOT ENOUGH BILLS\n" +
                    "Please chose a multiplier of: " + billValueToProcess);

            throw new AmountCanNotBeWithdrawnException("CURRENT AMOUNT CANNOT BE WITHDRAWN BECAUSE THERE ARE NOT ENOUGH BILLS\n" +
                    "Please chose a multiplier of: " + billValueToProcess);
        }
    }

    @Override
    public AtmResponseDTO getRequiredAmount(int amount) throws AmountCanNotBeWithdrawnException {
        Optional<List<CashDTO>> cashDtoListOptional = Optional.ofNullable(checkIfWeHaveAmount(amount));
        AtmResponseDTO atmResponseDTO = new AtmResponseDTO();
        if (cashDtoListOptional.isPresent()) {
            log.info("Required sum is made of: " + cashDtoListOptional.get());

            atmResponseDTO.setAmount(amount);
            atmResponseDTO.setCashDTOList(cashDtoListOptional.get());

            List<CashDTO> remainingCashResult = updateNrOfBillsInDb(cashDtoListOptional.get());

            List<String> alertNotificationList = cashNotificationService.notifyBillNumberDrop(remainingCashResult);
            alertNotificationList.forEach(System.out::println);
        } else {
            log.error("Operation cannot be made!");
        }

        return atmResponseDTO;

    }

    private List<CashDTO> updateNrOfBillsInDb(List<CashDTO> cashListToWithdraw) {
        List<CashDTO> remainingCashResult = cashListToWithdraw.stream()
                .map(cashToWithdraw -> CashDTO.builder()
                        .billValue(cashToWithdraw.getBillValue())
                        .numberOfAvailableBills(cashToWithdraw.getNumberOfAvailableBills())
                        .build())
                .collect(Collectors.toList());

        remainingCashResult.forEach(remainingCash -> {
            CashDTO availableCash = ConversionHelpers.cashEntityToDto(cashRepository.getCashByBillValue(remainingCash.getBillValue()));
            remainingCash.setNumberOfAvailableBills(availableCash.getNumberOfAvailableBills() - remainingCash.getNumberOfAvailableBills());
        });

        cashRepository.saveAllAndFlush(cashDtoListToCashList(remainingCashResult));

        return remainingCashResult;

    }
}
