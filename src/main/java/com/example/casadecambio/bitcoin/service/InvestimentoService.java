package com.example.casadecambio.bitcoin.service;

import com.example.casadecambio.bitcoin.model.Bitcoin;
import com.example.casadecambio.bitcoin.model.Compra;
import com.example.casadecambio.bitcoin.model.Data;
import com.example.casadecambio.bitcoin.model.Investimento;
import com.example.casadecambio.bitcoin.model.builder.InvestimentoBuilder;
import com.example.casadecambio.bitcoin.repository.CompraRepository;
import com.example.casadecambio.bitcoin.repository.InvestimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static java.util.Objects.nonNull;


@Service
public class InvestimentoService {

    @Autowired
    private InvestimentoRepository repository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private BitcoinService bitcoinService;

    @Autowired
    private InvestimentoRepository investimentoRepository;

    public Investimento getValorInvestido(Long id) {
        Investimento investimentoFound = repository.findById(id);
        List<Compra> compra = compraRepository.findByCpf(investimentoFound.getCpf());
        Mono<Bitcoin> bitcoinPrice = bitcoinService.getBitcoinPrice();

        BigDecimal getBitcoinAmount = bitcoinPrice.map(Bitcoin::getData)
                .map(Data::getAmount)
                .block();

        updateInvestimento(investimentoFound, compra, getBitcoinAmount);
        return investimentoFound;
    }

    private void updateInvestimento(Investimento investimentoFound, List<Compra> compra, BigDecimal getBitcoinAmount) {
        BigDecimal valorToTalCompra = getTotalDaCompra(compra);
        BigDecimal quantidadeDeBitcoins = getQuantidadeDeBitcoins(compra);
        BigDecimal getLucro = valorToTalCompra.add(getBitcoinAmount.negate());
        setInvestimento(valorToTalCompra, quantidadeDeBitcoins, getLucro, investimentoFound.getCpf());
    }

    private BigDecimal getTotalDaCompra(List<Compra> compra) {
        BigDecimal valorToTalCompra = ZERO;
        List<BigDecimal> listOfBitcoinPrice = compra.stream()
                .map(Compra::getValorDaCompra)
                .collect(Collectors.toList());

        for (int i = 0; i < listOfBitcoinPrice.size(); i++) {
            valorToTalCompra = valorToTalCompra.add(listOfBitcoinPrice.get(i));
        }
        return valorToTalCompra;
    }

    private BigDecimal getQuantidadeDeBitcoins(List<Compra> compra) {
        BigDecimal quantidadeDeBitcoins = ZERO;
        List<BigDecimal> listOfTotalDeBitcoins = compra.stream()
                .map(Compra::getQuantidadeDeBitcoins)
                .collect(Collectors.toList());
        for (int j = 0; j < listOfTotalDeBitcoins.size(); j++) {
            quantidadeDeBitcoins = quantidadeDeBitcoins.add(listOfTotalDeBitcoins.get(j));
        }
        return quantidadeDeBitcoins;
    }

    public Investimento setInvestimento(BigDecimal valorTotalDoBitcoin, BigDecimal quantidade, BigDecimal lucro, String cpf) {
        Investimento investimentoFound = repository.findByCpf(cpf);

        Investimento investimento = new InvestimentoBuilder()
                .setCpf(cpf)
                .setValorInvestido(valorTotalDoBitcoin.setScale(3, HALF_EVEN))
                .setQuantidadeInvestida(quantidade.setScale(3, HALF_EVEN))
                .setLucro(lucro.setScale(3, HALF_EVEN))
                .createInvestimento();

        if (nonNull(investimentoFound)) {
            investimentoFound.update(investimento);
            return investimentoRepository.save(investimentoFound);
        }
        return investimentoRepository.save(investimento);
    }
}