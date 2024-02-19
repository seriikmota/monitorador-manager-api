package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(6)
public class VMObrigatorio implements IVMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getData() == null)
            throw new ValidacaoException("A Data é obrigatória!");
        if (m.getEmail() == null)
            throw new ValidacaoException("O Email é obrigatório!");
        else {
            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.isValid(m.getEmail(), null))
                throw new ValidacaoException("Digite um email válido!");
        }
        if (m.getAtivo() == null)
            throw new ValidacaoException("O Status é obrigatório!");
    }
}
