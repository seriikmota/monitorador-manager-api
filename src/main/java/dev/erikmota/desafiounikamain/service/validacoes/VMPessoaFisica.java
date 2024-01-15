package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class VMPessoaFisica implements IValidacaoMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.FISICA) {
            if (m.getNome() == null || m.getNome().isBlank())
                throw new ValidacaoException("Pessoas físicas devem inserir o nome!");
            if (m.getRg() == null || m.getRg().isBlank())
                throw new ValidacaoException("Pessoas físicas devem inserir o rg!");
        }
    }
}