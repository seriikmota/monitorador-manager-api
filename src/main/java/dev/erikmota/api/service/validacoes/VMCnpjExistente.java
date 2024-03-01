package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.repository.MonitoradorRepository;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(5)
public class VMCnpjExistente implements IVMonitorador {
    private final MonitoradorRepository monitoradorRepository;

    public VMCnpjExistente(MonitoradorRepository monitoradorRepository) {
        this.monitoradorRepository = monitoradorRepository;
    }

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.JURIDICA) {
            if (m.getCnpj() != null){
                String cnpj = m.getCnpj().replaceAll("[^0-9]", "");
                if (monitoradorRepository.existsByCnpj(cnpj))
                    if (!Objects.equals(monitoradorRepository.findByCnpj(cnpj).getId(), m.getId()))
                        throw new ValidacaoException("Esse CNPJ já está cadastrado!");
            }
        }
    }
}
