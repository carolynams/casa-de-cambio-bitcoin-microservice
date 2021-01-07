package com.example.casadecambio.bitcoin.controller;


import com.example.casadecambio.bitcoin.model.Compra;
import com.example.casadecambio.bitcoin.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService service;

    @PostMapping("/")
    public Compra buyBitcoin(@RequestBody Compra compra) {
        return service.buyBitcoin(compra);
    }

    @GetMapping("/{cpf}")
    public List<Compra> findByCpf(@PathVariable("cpf") String cpf) {
        return service.findByCpf(cpf);
    }
}