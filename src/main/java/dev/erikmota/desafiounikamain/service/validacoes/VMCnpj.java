package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VMCnpj implements IValidacaoMonitorador {
    @Override
    public void validar(Monitorador m) {
        CNPJValidator validator = new CNPJValidator();
        validator.initialize(null);
        if (!validator.isValid(m.getCpf(), null))
            throw new ValidacaoException("Esse cnpj é inválido!");
    }
}
