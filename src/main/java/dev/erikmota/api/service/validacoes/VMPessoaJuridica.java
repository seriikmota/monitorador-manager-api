package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class VMPessoaJuridica implements IVMonitorador {

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.JURIDICA) {
            if (m.getCnpj() == null || m.getCnpj().isBlank())
                throw new ValidacaoException("Pessoa jurídica deve inserir o CNPJ!");
            if (m.getRazao() == null || m.getRazao().isBlank())
                throw new ValidacaoException("Pessoa jurídica deve inserir a Razao Social!");
            if (m.getInscricao() == null || m.getInscricao().isBlank())
                throw new ValidacaoException("Pessoa jurídica deve inserir a Inscrição Estadual!");
            m.setCpf(null);
            m.setNome(null);
            m.setRg(null);
        }
    }
}
