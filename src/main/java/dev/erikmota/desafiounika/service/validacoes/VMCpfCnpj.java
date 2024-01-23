package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class VMCpfCnpj implements IValidacaoMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == null)
            throw new ValidacaoException("O tipo da pessoa é obrigatório!");
        else {
            if (m.getTipo() == TipoPessoa.FISICA) {
                CPFValidator cpfValidator = new CPFValidator();
                cpfValidator.initialize(null);
                if (!cpfValidator.isValid(m.getCpf(), null))
                    throw new ValidacaoException("Esse cpf é inválido!");
            }
            else {
                CNPJValidator cnpjValidator = new CNPJValidator();
                cnpjValidator.initialize(null);
                if (!cnpjValidator.isValid(m.getCnpj(), null))
                    throw new ValidacaoException("Esse cnpj é inválido!");
            }
        }
    }
}
