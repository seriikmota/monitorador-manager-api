package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class VMPessoaJuridica implements IValidacaoMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.JURIDICA) {
            if (m.getCnpj() == null || m.getCnpj().isBlank())
                throw new ValidacaoException("Pessoas juridicas devem inserir cnpj!");
            if (m.getRazao() == null || m.getRazao().isBlank())
                throw new ValidacaoException("Pessoas jurídicas devem inserir a razao social!");
            if (m.getInscricao() == null || m.getInscricao().isBlank())
                throw new ValidacaoException("Pessoas jurídicas devem inserir a inscrição estadual!");
        }
    }
}
