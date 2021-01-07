package com.example.casadecambio.bitcoin.repository.implementation;

import com.example.casadecambio.bitcoin.model.Investimento;
import com.example.casadecambio.bitcoin.repository.InvestimentoRepository;
import com.example.casadecambio.bitcoin.repository.jpa.InvestimentoRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InvestimentoRespositoryImpl implements InvestimentoRepository {

    @Autowired
    private InvestimentoRepositoryJpa repositoryJpa;

    @Override
    public Investimento save(Investimento investimento) {
        return repositoryJpa.save(investimento);
    }

    @Override
    public Investimento findByCpf(String cpf) {
        return repositoryJpa.findByCpf(cpf);
    }

    @Override
    public Investimento findById(Long id) {
        Optional<Investimento> found = repositoryJpa.findById(id);
        return found.orElseThrow();
    }
}
