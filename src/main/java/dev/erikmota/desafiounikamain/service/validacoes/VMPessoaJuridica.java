package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class VMPessoaJuridica implements IValidacaoMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.JURIDICA) {
            if (m.getRazao() == null || m.getRazao().isBlank())
                throw new ValidacaoException("Pessoas jurídicas devem inserir a razao social!");
            if (m.getInscricao() == null || m.getInscricao().isBlank())
                throw new ValidacaoException("Pessoas jurídicas devem inserir a inscrição estadual!");
        }
    }
}
