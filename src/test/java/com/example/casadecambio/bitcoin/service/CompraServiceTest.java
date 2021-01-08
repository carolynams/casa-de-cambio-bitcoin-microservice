package com.example.casadecambio.bitcoin.service;

import com.example.casadecambio.bitcoin.model.Compra;
import com.example.casadecambio.bitcoin.model.builder.CompraBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CompraServiceTest {

    @InjectMocks
    private CompraService compraService;

    @Before
    public void executeThis() {
        openMocks(this);
    }

    @Test
    public void shoudlBuyBitcoin() {
        BigDecimal valor = valueOf(5098);
        BigDecimal quantidade = valueOf(1);
        String cpf = "102.663.619-19";

        Compra compra = createCompra(cpf, valor, quantidade);
        when(compraService.buyBitcoin(any(Compra.class))).thenReturn(compra);
        Compra saveCompra = compraService.buyBitcoin(compra);

        assertEquals(compra.getCpf(), saveCompra.getCpf());
        assertEquals(compra.getQuantidadeDeBitcoins(), saveCompra.getQuantidadeDeBitcoins());
        assertEquals(compra.getValorDaCompra(), saveCompra.getValorDaCompra());
    }

    private Compra createCompra(String cpf, BigDecimal valor, BigDecimal quantidade) {
        return new CompraBuilder()
                .setValorDaCompra(valor)
                .setQuantidade(quantidade)
                .setCpf(cpf)
                .createCompra();
    }
}
