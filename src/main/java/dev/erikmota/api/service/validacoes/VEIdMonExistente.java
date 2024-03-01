package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.repository.MonitoradorRepository;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class VEIdMonExistente implements IVEndereco {
    private final MonitoradorRepository monitoradorRepository;

    public VEIdMonExistente(MonitoradorRepository monitoradorRepository) {
        this.monitoradorRepository = monitoradorRepository;
    }

    @Override
    public void validar(Endereco e) {
        if (!monitoradorRepository.existsById(e.getMonitorador().getId())){
            throw new ValidacaoException("Esse monitorador não foi encontrado!");
        }
    }
}
