package com.bank.atm;

import com.bank.cash.CashDTO;
import lombok.Data;

import java.util.List;

@Data
public class AtmResponseDTO {

    private int amount;
    private List<CashDTO> cashDTOList;
}
