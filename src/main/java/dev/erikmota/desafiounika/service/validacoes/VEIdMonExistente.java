package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class VEIdMonExistente implements IVCadEndereco, IVEditarEndereco {
    @Autowired
    private MonitoradorRepository repository;

    @Override
    public void validar(Endereco e) {
        if (!repository.existsById(e.getMonitorador().getId())){
            throw new ValidacaoException("Esse monitorador não existe!");
        }
    }
}
