package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Order(5)
public class VEPrincipal implements IVEndereco {
    @Override
    public void validar(Endereco e) {
        List<Endereco> enderecoList = e.getMonitorador().getEnderecos();

        for (Endereco end : enderecoList){
            if (!Objects.equals(end.getCep(), e.getCep()))
                if (e.getPrincipal() && end.getPrincipal())
                    throw new ValidacaoException("Esse monitorador já possui endereço principal!");
        }
    }
}
