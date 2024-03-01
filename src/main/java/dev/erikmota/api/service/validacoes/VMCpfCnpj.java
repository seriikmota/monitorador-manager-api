package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CNPJValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class VMCpfCnpj implements IVMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == null)
            throw new ValidacaoException("O Tipo da Pessoa é obrigatório!");
        else {
            if (m.getTipo() == TipoPessoa.FISICA) {
                CPFValidator cpfValidator = new CPFValidator();
                cpfValidator.initialize(null);
                if (!cpfValidator.isValid(m.getCpf(), null) || m.getCpf().length() != 11)
                    throw new ValidacaoException("Esse CPF é inválido!");
            }
            else if (m.getTipo() == TipoPessoa.JURIDICA){
                CNPJValidator cnpjValidator = new CNPJValidator();
                cnpjValidator.initialize(null);
                if (!cnpjValidator.isValid(m.getCnpj(), null) || m.getCnpj().length() != 14)
                    throw new ValidacaoException("Esse CNPJ é inválido!");
            }
        }
    }
}
