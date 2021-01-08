package com.example.casadecambio.bitcoin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.springframework.beans.BeanUtils.copyProperties;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "COMPRA")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal quantidadeDeBitcoins;

    private BigDecimal valorDaCompra;

    @NotNull
    private String cpf;

    public Compra() {
    }

    public Compra(BigDecimal quantidadeDeBitcoins, BigDecimal valorDaCompra, String cpf) {
        this.quantidadeDeBitcoins = quantidadeDeBitcoins;
        this.valorDaCompra = valorDaCompra.setScale(3, RoundingMode.HALF_UP);
        this.cpf = cpf;
    }
}
