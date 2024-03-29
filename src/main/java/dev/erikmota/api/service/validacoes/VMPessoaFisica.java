package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VMPessoaFisica implements IVMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.FISICA) {
            if (m.getCpf() == null || m.getCpf().isBlank())
                throw new ValidacaoException("Pessoa física deve inserir CPF!");
            if (m.getNome() == null || m.getNome().isBlank())
                throw new ValidacaoException("Pessoa física deve inserir o Nome!");
            if (m.getRg() == null || m.getRg().isBlank())
                throw new ValidacaoException("Pessoa física deve inserir o Rg!");
            m.setCnpj(null);
            m.setRazao(null);
            m.setInscricao(null);
        }
    }
}