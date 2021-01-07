package com.example.casadecambio.bitcoin.controller;

import com.example.casadecambio.bitcoin.model.Investimento;
import com.example.casadecambio.bitcoin.service.InvestimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/investimento")
public class InvestimentoController {

    @Autowired
    private InvestimentoService service;

    @GetMapping("/{cpf}")
    public Mono<Investimento> getLucro(@PathVariable String cpf) {
        return service.getValorInvestido(cpf);
    }
}
