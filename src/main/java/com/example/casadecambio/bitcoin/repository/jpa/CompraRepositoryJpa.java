package com.example.casadecambio.bitcoin.repository.jpa;

import com.example.casadecambio.bitcoin.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepositoryJpa extends JpaRepository<Compra, Long> {

    List<Compra> findByCpf(String cpf);

}
