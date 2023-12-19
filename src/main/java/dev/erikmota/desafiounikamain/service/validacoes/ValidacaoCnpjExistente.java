package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoCnpjExistente implements IValidacaoMonitorador {

    @Autowired
    private MonitoradorRepository repository;

    @Override
    public void validar(Monitorador m) {
        if (m.getTipoPessoa() == TipoPessoa.JURIDICA) {
            if (m.getCnpj() == null || m.getCnpj().isBlank())
                throw new ValidacaoException("Pessoas juridicas devem inserir cnpj!");

            else if (repository.existsByCnpj(m.getCnpj().replaceAll("[^0-9]", "")))
                throw new ValidacaoException("Esse Cnpj já está cadastrado!");
        }
    }
}
