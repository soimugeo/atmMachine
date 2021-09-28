package com.bank.cash;

import com.bank.atm.AtmResponseDTO;
import com.bank.exceptions.AmountCanNotBeWithdrawnException;

import java.util.List;

public interface CashService {

    List<CashDTO> checkIfWeHaveAmount(int amount) throws AmountCanNotBeWithdrawnException;
    AtmResponseDTO getRequiredAmount(int amount) throws AmountCanNotBeWithdrawnException;
}
