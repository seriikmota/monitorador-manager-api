package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VEEndereco implements IValidacaoEndereco {
    @Autowired
    private EnderecoRepository repository;

    @Override
    public void validar(Endereco e) {
        if (repository.existsByCep(e.getCep().replaceAll("[^0-9]", "")))
            if (repository.existsByEndereco(e.getEndereco()))
                throw new ValidacaoException("O campo endereço já existe, especifique mais!");
    }
}
