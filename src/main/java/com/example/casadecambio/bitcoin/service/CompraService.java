package com.example.casadecambio.bitcoin.service;

import com.example.casadecambio.bitcoin.model.Bitcoin;
import com.example.casadecambio.bitcoin.model.Compra;
import com.example.casadecambio.bitcoin.model.builder.CompraBuilder;
import com.example.casadecambio.bitcoin.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;

@Service
public class CompraService {

    @Autowired
    private CompraRepository repository;

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private InvestimentoService investimentoService;

    public Compra buyBitcoin(Compra compra) {
        return repository.save(getBitcoinPurchaseValue(compra.getQuantidadeDeBitcoins(), compra.getCpf()).block());
    }

    private Mono<Compra> getBitcoinPurchaseValue(BigDecimal quantidade, String cpf) {
        Mono<Bitcoin> bitcoinPrice = getBitcoinPrice();
        Mono<Compra> compra = bitcoinPrice
                .map(Bitcoin::getData)
                .map(bitcoin -> {
                    BigDecimal valorDaCompra = bitcoin.getAmount().multiply(quantidade);
                    Compra saveCompra = new CompraBuilder()
                            .setCpf(cpf)
                            .setQuantidade(quantidade)
                            .setValorDaCompra(valorDaCompra)
                            .createCompra();
                    investimentoService.setInvestimento(saveCompra.getValorDaCompra(), ZERO, quantidade, cpf);
                    return saveCompra;
                });
        return compra;
    }

    public Mono<Bitcoin> getBitcoinPrice() {
        return bitcoinService.getBitcoinPrice();
    }

    public List<Compra> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
