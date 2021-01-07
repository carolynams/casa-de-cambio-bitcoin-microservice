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

    public Mono<Investimento> getValorInvestido(String cpf) {
        Mono<Bitcoin> bitcoinPrice = bitcoinService.getBitcoinPrice();
        Compra compra = compraRepository.findByCpf(cpf);

        return bitcoinPrice
                .map(Bitcoin::getData)
                .map(bitcoin -> {
                    float valorDaCompraDeBitcoin = compra.getValorDaCompra().setScale(3, HALF_EVEN).floatValue();
                    float valorAtualBitcoin = bitcoin.getAmount().setScale(3, HALF_EVEN).floatValue();
                    Investimento investimento = new InvestimentoBuilder()
                            .setCpf(cpf)
                            .setTipo(bitcoin.getBase())
                            .setValorInvestido(compra.getValorDaCompra())
                            .setQuantidadeInvestida(compra.getQuantidadeDeBitcoins())
                            .setLucro(valueOf(valorAtualBitcoin - valorDaCompraDeBitcoin))
                            .setDataDoInvestimento(compra.getHorarioDaTransacao())
                            .setCotacaoAtualBitcoin(bitcoin.getAmount())
                            .createInvestimento();
                   return repository.save(investimento);
                });
    }
}
