package com.bank.cash;

import com.bank.atm.AtmResponseDTO;
import com.bank.exceptions.AmountCanNotBeWithdrawnException;

import java.util.List;

public interface CashService {

    public List<CashDTO> checkIfWeHaveAmount(int amount) throws AmountCanNotBeWithdrawnException;
    public AtmResponseDTO getRequiredAmount(String amount) throws AmountCanNotBeWithdrawnException;
}
