package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.repository.EnderecoRepository;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VEEndereco implements IVEndereco {
    @Autowired
    private EnderecoRepository repository;

    @Override
    public void validar(Endereco e) {
        if (repository.existsByEnderecoAndNumero(e.getEndereco(), e.getNumero()))
            throw new ValidacaoException("Esse endereço já existe, altere o campo endereço e/ou número!");
    }
}
