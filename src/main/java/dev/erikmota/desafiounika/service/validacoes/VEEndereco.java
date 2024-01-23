package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.repository.EnderecoRepository;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VEEndereco implements IVCadEndereco, IVEditarEndereco {
    @Autowired
    private EnderecoRepository repository;

    @Override
    public void validar(Endereco e) {
        if (repository.existsByCep(e.getCep().replaceAll("[^0-9]", "")))
            if (repository.existsByEndereco(e.getEndereco()))
                throw new ValidacaoException("O campo Endereço já existe, especifique mais!");
    }
}
