package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class VEEndereco implements IVEndereco {
    @Autowired
    private EnderecoRepository repository;

    @Override
    public void validar(Endereco e) {
        /*String cep = e.getCep().replaceAll("[^0-9]", "");
        if (repository.existsByCep(cep))
            if (!Objects.equals(e.getId(), repository.findByCep(cep).getId()))
                if (repository.existsByEndereco(e.getEndereco()))
                    throw new ValidacaoException("O campo Endereço já existe, especifique mais!");*/

    }
}
