package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(5)
public class VMCnpjExistente implements IVMonitorador {
    private final MonitoradorDAO monitoradorDAO;

    public VMCnpjExistente(MonitoradorDAO monitoradorDAO) {
        this.monitoradorDAO = monitoradorDAO;
    }

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.JURIDICA) {
            if (m.getCnpj() != null){
                String cnpj = m.getCnpj().replaceAll("[^0-9]", "");
                if (monitoradorDAO.existsByCnpj(cnpj))
                    if (!Objects.equals(monitoradorDAO.findByCnpj(cnpj).getId(), m.getId()))
                        throw new ValidacaoException("Esse CNPJ já está cadastrado!");
            }
        }
    }
}
