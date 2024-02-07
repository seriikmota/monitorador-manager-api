package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Order(5)
public class VMCnpjExistente implements IVMonitorador {

    private final MonitoradorRepository repository;

    public VMCnpjExistente(MonitoradorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validar(Monitorador m) {
        if (m.getTipo() == TipoPessoa.JURIDICA) {
            if (m.getCnpj() != null){
                String cnpj = m.getCnpj().replaceAll("[^0-9]", "");
                if (repository.existsByCnpj(cnpj))
                    if (!Objects.equals(repository.findByCnpj(cnpj).getId(), m.getId()))
                        throw new ValidacaoException("Esse CNPJ já está cadastrado!");
            }
        }
    }
}
