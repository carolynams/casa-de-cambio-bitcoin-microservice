package com.example.casadecambio.bitcoin.service;

import com.example.casadecambio.bitcoin.model.Bitcoin;
import com.example.casadecambio.bitcoin.model.Compra;
import com.example.casadecambio.bitcoin.model.Investimento;
import com.example.casadecambio.bitcoin.model.builder.CompraBuilder;
import com.example.casadecambio.bitcoin.model.builder.InvestimentoBuilder;
import com.example.casadecambio.bitcoin.repository.CompraRepository;
import com.example.casadecambio.bitcoin.repository.InvestimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.time.LocalDateTime.now;

@Service
public class CompraService {

    @Autowired
    private CompraRepository repository;

    @Autowired
    private InvestimentoRepository investimentoRepository;

    @Autowired
    private BitcoinService bitcoinService;

    public Compra buyBitcoin(Compra compra) {
        return repository.save(getBitcoinPurchaseValue(compra.getQuantidadeDeBitcoins(), compra.getCpf()).block());
    }

    private Mono<Compra> getBitcoinPurchaseValue(BigDecimal quantidade, String cpf) {
        Mono<Bitcoin> bitcoinPrice = getBitcoinPrice();
        Mono<Compra> compra = bitcoinPrice
                .map(Bitcoin::getData)
                .map(bitcoin -> {
                    BigDecimal valorDaCompra = bitcoin.getAmount().multiply(quantidade);
                    return new CompraBuilder()
                            .setCpf(cpf)
                            .setQuantidade(quantidade)
                            .setValorDaCompra(valorDaCompra)
                            .createCompra();
                });
        Mono<Investimento> investimentoMono = setInvestimento(compra.block().getValorDaCompra(), quantidade, cpf);
        investimentoRepository.save(investimentoMono.block());
        return compra;
    }

    public Mono<Bitcoin> getBitcoinPrice() {
        return bitcoinService.getBitcoinPrice();
    }

    public Compra findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    private Mono<Investimento> setInvestimento(BigDecimal valorTotalDoBitcoin, BigDecimal quantidade, String cpf) {
        Mono<Bitcoin> bitcoinPrice = getBitcoinPrice();

        return bitcoinPrice
                .map(Bitcoin::getData)
                .map(bitcoin -> new InvestimentoBuilder()
                        .setCpf(cpf)
                        .setTipo(bitcoin.getBase())
                        .setValorInvestido(valorTotalDoBitcoin.setScale(3, HALF_EVEN))
                        .setQuantidadeInvestida(quantidade)
                        .setLucro(ZERO)
                        .setDataDoInvestimento(now())
                        .setCotacaoAtualBitcoin(bitcoin.getAmount())
                        .createInvestimento());

    }
}
