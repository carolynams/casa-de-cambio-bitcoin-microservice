package com.example.casadecambio.cadastro.service;

import br.com.caelum.stella.validation.CPFValidator;
import com.example.casadecambio.cadastro.exceptions.DataIntegrityViolationException;
import com.example.casadecambio.cadastro.model.Cliente;
import com.example.casadecambio.cadastro.model.Conta;
import com.example.casadecambio.cadastro.model.dto.ClienteDTO;
import com.example.casadecambio.cadastro.repository.ClienteRepository;
import com.example.casadecambio.cadastro.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    public Cliente save(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente().fromDTO(clienteDTO);
        String cpf = cliente.getCpf();
        validateCpf(cpf);
        checkIfAlreadyExists(cliente);
        return clienteRepository.save(cliente);
    }

    private void checkIfAlreadyExists(Cliente cliente) {
        List<Cliente> cpfFound = clienteRepository.findByCpf(cliente.getCpf());
        if (cpfFound.size() >= 1) {
            throw new DataIntegrityViolationException(DataIntegrityViolationException.CPF_JA_CADASTRADO);
        }

        Conta conta = cliente.getConta();
        Set<Conta> foundConta = contaRepository.findByContaOrderByClientesNomeAsc(conta.getConta());
        if (foundConta.size() >= 1) {
            throw new DataIntegrityViolationException(DataIntegrityViolationException.CONTA_CADASTRADA);
        }
    }

    private void validateCpf(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
        } catch (Exception e) {
            throw new DataIntegrityViolationException(DataIntegrityViolationException.CPF_INVALIDO);
        }
    }
}