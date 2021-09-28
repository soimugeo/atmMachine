package com.bank.helpers;

import com.bank.cash.CashDTO;
import com.bank.cash.Cash;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ConversionHelpers {

    private static ModelMapper modelMapper = new ModelMapper();

    public static CashDTO cashEntityToDto(Cash cash){
        return modelMapper.map(cash, CashDTO.class);
    }
    public static Cash cashDtoToCashEntity(CashDTO cashDTO){
        return modelMapper.map(cashDTO, Cash.class);
    }

    public static List<CashDTO> cashListToCashDtoList(List<Cash> cashList){
        return cashList.stream().map(ConversionHelpers::cashEntityToDto).collect(Collectors.toList());
    }

    public static List<Cash> cashDtoListToCashList(List<CashDTO> cashDtoList){
        return cashDtoList.stream().map(ConversionHelpers::cashDtoToCashEntity).collect(Collectors.toList());
    }

}
