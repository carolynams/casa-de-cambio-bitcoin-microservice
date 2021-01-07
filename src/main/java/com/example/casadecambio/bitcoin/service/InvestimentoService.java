package com.example.casadecambio.bitcoin.service;

import com.example.casadecambio.bitcoin.model.Bitcoin;
import com.example.casadecambio.bitcoin.model.Compra;
import com.example.casadecambio.bitcoin.model.Investimento;
import com.example.casadecambio.bitcoin.model.builder.InvestimentoBuilder;
import com.example.casadecambio.bitcoin.repository.CompraRepository;
import com.example.casadecambio.bitcoin.repository.InvestimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_EVEN;


@Service
public class InvestimentoService {

    @Autowired
    private InvestimentoRepository repository;

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private BitcoinService bitcoinService;

    public Investimento getValorInvestido(Long id) {
        Mono<Bitcoin> bitcoinPrice = bitcoinService.getBitcoinPrice();
        Investimento investimentoFound = repository.findById(id);
        List<Compra> compra = compraRepository.findByCpf(investimentoFound.getCpf());

        List<BigDecimal> valorDasCompras = new ArrayList<>();
        List<BigDecimal> totalDeBitcoins = new ArrayList<>();

        compra.forEach(c -> bitcoinPrice
                .map(Bitcoin::getData)
                .map(bitcoin -> {

                    compra.forEach(compraDTO -> {
                        BigDecimal valorToTalCompra = ZERO.setScale(3, HALF_EVEN);
                        BigDecimal quantidadeDeBitcoins = ZERO.setScale(3, HALF_EVEN);

                        valorDasCompras.add(compraDTO.getValorDaCompra());
                        for (int i = 0; i < valorDasCompras.size(); i++) {
                            valorToTalCompra.add(valorDasCompras.get(i));
                        }
                        totalDeBitcoins.add(compraDTO.getQuantidadeDeBitcoins());
                        for (int i = 0; i < totalDeBitcoins.size(); i++) {
                            quantidadeDeBitcoins.add(totalDeBitcoins.get(i));
                        }

                        float valorDaCompraDeBitcoin = valorToTalCompra.floatValue();
                        float valorAtualBitcoin = bitcoin.getAmount().setScale(3, HALF_EVEN).floatValue();

                        Investimento investimento = new InvestimentoBuilder()
                                .setCpf(investimentoFound.getCpf())
                                .setTipo(bitcoin.getBase())
                                .setValorInvestido(valorToTalCompra)
                                .setQuantidadeInvestida(quantidadeDeBitcoins)
                                .setLucro(valueOf(valorAtualBitcoin - valorDaCompraDeBitcoin))
                                .setCotacaoAtualBitcoin(bitcoin.getAmount())
                                .createInvestimento();
                        investimentoFound.update(investimento);
                        repository.save(investimentoFound);
                    });
                    return investimentoFound;
                }));
        return investimentoFound;
    }
}