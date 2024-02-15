package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(4)
public class VMCpfExistente implements IVMonitorador {

    private final MonitoradorDAO monitoradorDAO;

    public VMCpfExistente(MonitoradorDAO monitoradorDAO) {
        this.monitoradorDAO = monitoradorDAO;
    }

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.FISICA)
            if (m.getCpf() != null){
                String cpf = m.getCpf().replaceAll("[^0-9]", "");
                if (monitoradorDAO.existsByCpf(cpf))
                    if (!Objects.equals(monitoradorDAO.findByCpf(cpf).getId(), m.getId()))
                        throw new ValidacaoException("Esse CPF já está cadastrado!");

            }
    }
}
