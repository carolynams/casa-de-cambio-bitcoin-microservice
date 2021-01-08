package com.example.casadecambio.bitcoin.service;

import com.example.casadecambio.bitcoin.model.Investimento;
import com.example.casadecambio.bitcoin.model.builder.InvestimentoBuilder;
import com.example.casadecambio.bitcoin.repository.CompraRepository;
import com.example.casadecambio.bitcoin.repository.InvestimentoRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import static com.example.casadecambio.bitcoin.service.BitcoinService.URL;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class InvestimentoServiceTest {

    @InjectMocks
    private InvestimentoService service;

    @Mock
    private InvestimentoRepository repository;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private BitcoinService bitcoinService;

    @Before
    public void executeThis() {
        openMocks(this);
    }

    @Test
    public void shouldGetValorInvestido() {
        BigDecimal valorTotalDoBitcoin = valueOf(154);
        BigDecimal quantidade = valueOf(1);
        BigDecimal lucro = valueOf(0);
        String cpf = "102.663.619-19";

        Investimento investimento = createInvestimento(valorTotalDoBitcoin, quantidade, lucro, cpf);
        when(repository.findById(1L)).thenReturn(investimento);

        Investimento saveInvestimento = service.getValorInvestido(1L);
        assertEquals(investimento.getCpf(), saveInvestimento.getCpf());
        assertEquals(investimento.getQuantidadeInvestida(), saveInvestimento.getQuantidadeInvestida());
        assertEquals(investimento.getLucro(), saveInvestimento.getLucro());
        assertEquals(investimento.getValorInvestido(), saveInvestimento.getValorInvestido());
    }

    public Investimento createInvestimento(BigDecimal valorTotalDoBitcoin, BigDecimal quantidade, BigDecimal lucro, String cpf) {
        return new InvestimentoBuilder()
                .setValorInvestido(valorTotalDoBitcoin)
                .setQuantidadeInvestida(quantidade)
                .setLucro(lucro)
                .setCpf(cpf)
                .createInvestimento();
    }


}
