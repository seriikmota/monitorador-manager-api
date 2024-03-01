package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.repository.EnderecoRepository;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VEEndereco implements IVEndereco {
    private final EnderecoRepository enderecoRepository;

    public VEEndereco(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Override
    public void validar(Endereco e) {
        if (enderecoRepository.existsByEnderecoAndNumero(e.getEndereco(), e.getNumero()))
            throw new ValidacaoException("Esse endereço já existe, altere o campo endereço e/ou número!");
    }
}
