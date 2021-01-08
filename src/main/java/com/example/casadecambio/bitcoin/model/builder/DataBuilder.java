package com.example.casadecambio.bitcoin.model.builder;

import com.example.casadecambio.bitcoin.model.Data;

import java.math.BigDecimal;

public class DataBuilder {
    private BigDecimal amount;
    private String currency;
    private String base;

    public DataBuilder setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public DataBuilder setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public DataBuilder setBase(String base) {
        this.base = base;
        return this;
    }

    public Data createData() {
        return new Data(amount, currency, base);
    }
}