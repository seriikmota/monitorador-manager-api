package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class VEIdMonExistente implements IVEndereco {
    private final MonitoradorDAO monitoradorDAO;

    public VEIdMonExistente(MonitoradorDAO monitoradorDAO) {
        this.monitoradorDAO = monitoradorDAO;
    }

    @Override
    public void validar(Endereco e) {
        if (!monitoradorDAO.existsById(e.getMonitorador().getId())){
            throw new ValidacaoException("Esse monitorador não existe!");
        }
    }
}
