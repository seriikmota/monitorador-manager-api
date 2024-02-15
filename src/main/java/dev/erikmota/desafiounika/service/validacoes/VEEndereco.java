package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.dao.EnderecoDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VEEndereco implements IVEndereco {
    private final EnderecoDAO enderecoDAO;

    public VEEndereco(EnderecoDAO enderecoDAO) {
        this.enderecoDAO = enderecoDAO;
    }

    @Override
    public void validar(Endereco e) {
        if (enderecoDAO.existsByEnderecoAndNumero(e.getEndereco(), e.getNumero()))
            throw new ValidacaoException("Esse endereço já existe, altere o campo endereço e/ou número!");
    }
}
