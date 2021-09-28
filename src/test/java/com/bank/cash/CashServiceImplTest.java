package com.bank.cash;

import com.bank.exceptions.AmountCanNotBeWithdrawnException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


class CashServiceImplTest {

    @Mock
    private CashRepository cashRepository;

    private CashServiceImpl cashService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cashService = new CashServiceImpl(cashRepository);

        List<Cash> mockedList = new ArrayList<>();
        mockedList.add(Cash.builder()
                .billValue(1)
                .numberOfAvailableBills(100)
                .build());

        mockedList.add(Cash.builder()
                .billValue(5)
                .numberOfAvailableBills(100)
                .build());

        mockedList.add(Cash.builder()
                .billValue(10)
                .numberOfAvailableBills(100)
                .build());

        mockedList.add(Cash.builder()
                .billValue(50)
                .numberOfAvailableBills(50)
                .build());

        mockedList.add(Cash.builder()
                .billValue(100)
                .numberOfAvailableBills(50)
                .build());


        Mockito.when(cashRepository.findAll()).thenReturn(mockedList);
    }

    @AfterEach
    void tearDown() {


    }

    @Test
    void checkIfWeHaveAmount() {
        int withdrawAmount = 525;
        int exceedingAmount = 9150;

        List<CashDTO> expectedResult = new ArrayList<>();

        expectedResult.add(CashDTO.builder()
                .billValue(100)
                .numberOfAvailableBills(5)
                .build());

        expectedResult.add(CashDTO.builder()
                .billValue(10)
                .numberOfAvailableBills(2)
                .build());

        expectedResult.add(CashDTO.builder()
                .billValue(5)
                .numberOfAvailableBills(1)
                .build());


        try {
            Assertions.assertEquals(expectedResult, cashService.checkIfWeHaveAmount(withdrawAmount));
        } catch (AmountCanNotBeWithdrawnException e) {
            e.printStackTrace();
        }


        try {
            Assertions.assertNull(cashService.checkIfWeHaveAmount(exceedingAmount));
        } catch (AmountCanNotBeWithdrawnException e) {
            e.printStackTrace();
        }

        List<Cash> someCashList = new ArrayList<>();

        someCashList.add(Cash.builder()
                .billValue(25)
                .numberOfAvailableBills(1)
                .build());

        someCashList.add(Cash.builder()
                .billValue(100)
                .numberOfAvailableBills(1)
                .build());

        int correctAmountWithoutRequiredNrOfBills = 50;

        Mockito.when(cashRepository.findAll()).thenReturn(someCashList);

        Assertions.assertThrows(AmountCanNotBeWithdrawnException.class, () -> cashService.checkIfWeHaveAmount(correctAmountWithoutRequiredNrOfBills));


    }

    @Test
    void getRequiredAmount() {

    }
}