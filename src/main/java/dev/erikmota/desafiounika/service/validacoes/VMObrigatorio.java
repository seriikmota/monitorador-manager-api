package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(6)
public class VMObrigatorio implements IValidacaoMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getData() == null)
            throw new ValidacaoException("O data é obrigatório!");
        if (m.getEmail() == null)
            throw new ValidacaoException("O email é obrigatório!");
        else {
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.isValid(m.getEmail(), null))
                throw new ValidacaoException("Digite um email válido!");
        }
        if (m.getAtivo() == null)
            throw new ValidacaoException("O ativo é obrigatório!");
    }
}
