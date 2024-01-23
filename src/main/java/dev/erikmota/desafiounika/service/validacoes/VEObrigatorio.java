package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(1)
public class VEObrigatorio implements IVCadEndereco, IVEditarEndereco {
    @Override
    public void validar(Endereco e) {
        if (e.getCep() == null)
            throw new ValidacaoException("O CEP é obrigatório!");
        else if (e.getEndereco() == null)
            throw new ValidacaoException("O Endereço é obrigatório!");
        else if (e.getNumero() == null)
            throw new ValidacaoException("O Número é obrigatório!");
        else if (e.getBairro() == null)
            throw new ValidacaoException("O Bairro é obrigatório!");
        else if (e.getCidade() == null)
            throw new ValidacaoException("A Cidade é obrigatória!");
        else if (e.getEstado() == null)
            throw new ValidacaoException("O Estado é obrigatório!");
        else if (e.getTelefone() == null)
            throw new ValidacaoException("O Telefone é obrigatório!");
        else if (e.getMonitorador() == null)
            throw new ValidacaoException("O Monitorador deve ser definido!");
        else if (e.getPrincipal() == null)
            throw new ValidacaoException("O campo Principal é obrigatório!");
    }
}
