package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class VMCpfCnpj implements IValidacaoMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getCpf() == null && m.getCnpj() == null)
            throw new ValidacaoException("É necessário inserir cpf e/ou cnpj!");

        if (m.getTipo() != null){
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
