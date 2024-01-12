package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class VMCpf implements IValidacaoMonitorador {
    @Override
    public void validar(Monitorador m) {
        CPFValidator validator = new CPFValidator();
        validator.initialize(null);
        if (!validator.isValid(m.getCpf(), null))
            throw new ValidacaoException("Esse cpf é inválido!");
    }
}
