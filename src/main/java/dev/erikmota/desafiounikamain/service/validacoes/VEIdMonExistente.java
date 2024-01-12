package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VEIdMonExistente implements IValidacaoEndereco {
    @Autowired
    private MonitoradorRepository repository;

    @Override
    public void validar(Endereco e) {
        if (!repository.existsById(e.getMonitorador().getId())){
            throw new ValidacaoException("Esse monitorador não existe!");
        }
    }
}
