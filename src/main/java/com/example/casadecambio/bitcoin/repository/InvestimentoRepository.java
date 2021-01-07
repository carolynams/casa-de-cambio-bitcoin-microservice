package com.example.casadecambio.bitcoin.repository;

import com.example.casadecambio.bitcoin.model.Investimento;
import java.util.*;

public interface InvestimentoRepository {

    Investimento save(Investimento investimento);

    Investimento findByCpf(String cpf);

    Investimento findById(Long id);
}
