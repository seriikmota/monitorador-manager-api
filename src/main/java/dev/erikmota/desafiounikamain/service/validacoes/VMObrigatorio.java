package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class VMObrigatorio implements IValidacaoMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == null)
            throw new ValidacaoException("O tipo da pessoa é obrigatório!");
        if (m.getData() == null)
            throw new ValidacaoException("O data é obrigatório!");
        if (m.getEmail() == null)
            throw new ValidacaoException("O email é obrigatório!");
        if (m.getAtivo() == null)
            throw new ValidacaoException("O ativo é obrigatório!");
    }
}
