package com.example.casadecambio.bitcoin.repository;

import com.example.casadecambio.bitcoin.model.Compra;
import java.util.*;

public interface CompraRepository {

    Compra save(Compra compra);

    List<Compra> findByCpf(String cpf);
}
