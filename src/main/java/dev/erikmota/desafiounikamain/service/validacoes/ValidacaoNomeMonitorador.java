package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoNomeMonitorador implements IValidacaoMonitoradorFisico{

    @Autowired
    private MonitoradorRepository repository;

    @Override
    public void validar(Monitorador m) {
        if (repository.existsByNome(m.getNome()))
            throw new ValidacaoException("O campo nome é obrigatório para pessoas físicas");
    }
}
