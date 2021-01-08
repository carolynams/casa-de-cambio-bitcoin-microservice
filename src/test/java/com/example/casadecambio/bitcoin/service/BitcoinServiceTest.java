package com.example.casadecambio.bitcoin.service;

import com.example.casadecambio.bitcoin.model.Bitcoin;
import com.example.casadecambio.bitcoin.model.Data;
import com.example.casadecambio.bitcoin.model.builder.BitcoinBuilder;
import com.example.casadecambio.bitcoin.model.builder.DataBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import static com.example.casadecambio.bitcoin.service.BitcoinService.BRL_PRICE;
import static com.example.casadecambio.bitcoin.service.BitcoinService.URL;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.openMocks;

@WebFluxTest
public class BitcoinServiceTest {

    private WebClient webClient;

    private ExchangeFunction exchangeFunction;

    @Before
    public void executeThis() {
        openMocks(this);
        this.exchangeFunction = mock(ExchangeFunction.class);
        this.webClient = WebClient.create(URL);
    }

    @Test
    public void shoudlGetBitcoinPrice() {
        webClient.get()
                .uri(BRL_PRICE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();
    }

    private Bitcoin createBitcoin() {
        Data data = new DataBuilder()
                .setAmount(BigDecimal.valueOf(1))
                .setBase("BTC")
                .setCurrency("BRL")
                .createData();

        return new BitcoinBuilder()
                .setData(data)
                .createBitcoin();
    }

}
