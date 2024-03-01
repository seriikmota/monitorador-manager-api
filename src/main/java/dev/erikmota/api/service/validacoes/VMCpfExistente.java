package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.repository.MonitoradorRepository;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(4)
public class VMCpfExistente implements IVMonitorador {

    private final MonitoradorRepository monitoradorRepository;

    public VMCpfExistente(MonitoradorRepository monitoradorRepository) {
        this.monitoradorRepository = monitoradorRepository;
    }

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.FISICA)
            if (m.getCpf() != null){
                String cpf = m.getCpf().replaceAll("[^0-9]", "");
                if (monitoradorRepository.existsByCpf(cpf))
                    if (!Objects.equals(monitoradorRepository.findByCpf(cpf).getId(), m.getId()))
                        throw new ValidacaoException("Esse CPF já está cadastrado!");

            }
    }
}
