package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(5)
public class VEPrincipal implements IValidacaoEndereco {
    @Override
    public void validar(Endereco e) {
        List<Endereco> enderecoList = e.getMonitorador().getEnderecos();
        for (Endereco end : enderecoList){
            if (e.getPrincipal() && end.getPrincipal()){
                throw new ValidacaoException("Esse monitorador já possui endereço principal!");
            }
        }
    }
}
