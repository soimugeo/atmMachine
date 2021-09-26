package com.bank.cash;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
@Builder
public class Cash {
    private int billValue;
    private long numberOfAvailableBills;
}
