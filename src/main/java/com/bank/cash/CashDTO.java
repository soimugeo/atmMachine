package com.bank.cash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashDTO implements Comparable<CashDTO>{
    private int billValue;
    private long numberOfAvailableBills;


    @Override
    public int compareTo(CashDTO o) {
        return this.billValue - o.billValue;
    }
}
