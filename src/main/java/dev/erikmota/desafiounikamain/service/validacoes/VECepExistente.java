package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VECepExistente implements IValidacaoEndereco{

    @Autowired
    private EnderecoRepository repository;

    @Override
    public void validar(Endereco e) {
        if (repository.existsByCep(e.getCep()))
            throw new ValidacaoException("Esse Cep já está cadastrado!");
    }
}
