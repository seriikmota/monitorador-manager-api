package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
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
