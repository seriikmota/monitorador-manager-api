package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class VETelefone implements IVEndereco {
    @Override
    public void validar(Endereco e) {
        String telefone = e.getTelefone().replaceAll("[^0-9]", "");
        if (telefone.length() != 11)
            throw new ValidacaoException("Digite um número de telefone válido!");
    }
}
