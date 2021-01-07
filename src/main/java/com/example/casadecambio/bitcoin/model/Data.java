package com.example.casadecambio.bitcoin.model;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

@lombok.Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Data {

    private BigDecimal amount;

    private String currency;

    private String base;

    public Data() {
    }

    public Data(BigDecimal amount, String currency, String base) {
        this.amount = amount.setScale(3, RoundingMode.HALF_UP);
        this.currency = currency;
        this.base = base;
    }
}
