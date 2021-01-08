package com.example.casadecambio.bitcoin.model.builder;

import com.example.casadecambio.bitcoin.model.Investimento;

import java.math.BigDecimal;

public class InvestimentoBuilder {
    private BigDecimal valorInvestido;
    private BigDecimal quantidadeInvestida;
    private BigDecimal lucro;
    private String cpf;

    public InvestimentoBuilder setValorInvestido(BigDecimal valorInvestido) {
        this.valorInvestido = valorInvestido;
        return this;
    }

    public InvestimentoBuilder setQuantidadeInvestida(BigDecimal quantidadeInvestida) {
        this.quantidadeInvestida = quantidadeInvestida;
        return this;
    }

    public InvestimentoBuilder setLucro(BigDecimal lucro) {
        this.lucro = lucro;
        return this;
    }


    public InvestimentoBuilder setCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public Investimento createInvestimento() {
        return new Investimento(valorInvestido, quantidadeInvestida, lucro, cpf);
    }
}