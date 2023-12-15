package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoCnpjCpf implements IValidacaoMonitorador {
    @Override
    public void validar(Monitorador m) {
        if (m.getTipoPessoa() == TipoPessoa.FISICA) {
            try {
                if (m.getNome().isBlank())
                    throw new ValidacaoException("Pessoas físicas devem inserir o nome");
            } catch (NullPointerException ex) {
                throw new ValidacaoException("Pessoas físicas devem inserir o nome");
            }
        } else if (m.getTipoPessoa() == TipoPessoa.JURIDICA) {
            try {
                if (m.getRazaoSocial().isBlank())
                    throw new ValidacaoException("Pessoas jurídicas devem inserir a razao social");
            } catch (NullPointerException ex) {
                throw new ValidacaoException("Pessoas jurídicas devem inserir a razao social");
            }
        }
    }
}