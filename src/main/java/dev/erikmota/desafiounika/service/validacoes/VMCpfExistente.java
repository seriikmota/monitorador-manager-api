package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class VMCpfExistente implements IVCadMonitorador {

    @Autowired
    private MonitoradorRepository repository;

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.FISICA)
            if (m.getCpf() != null)
                if (repository.existsByCpf(m.getCpf().replaceAll("[^0-9]", "")))
                    throw new ValidacaoException("Esse CPF já está cadastrado!");
    }
}
