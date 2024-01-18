package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.apache.poi.hssf.record.DVALRecord;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Order(1)
public class VEObrigatorio implements IValidacaoEndereco {
    @Override
    public void validar(Endereco e) {
        if (e.getCep() == null)
            throw new ValidacaoException("O cep é obrigatório!");
        else if (e.getEndereco() == null)
            throw new ValidacaoException("O endereço é obrigatório!");
        else if (e.getNumero() == null)
            throw new ValidacaoException("O número é obrigatório!");
        else if (e.getBairro() == null)
            throw new ValidacaoException("O bairro é obrigatório!");
        else if (e.getCidade() == null)
            throw new ValidacaoException("A cidade é obrigatória!");
        else if (e.getEstado() == null)
            throw new ValidacaoException("O estado é obrigatório!");
        else if (e.getTelefone() == null)
            throw new ValidacaoException("O telefone é obrigatório!");
        else if (e.getMonitorador() == null)
            throw new ValidacaoException("O monitorador deve ser definido!");
        else if (e.getPrincipal() == null)
            throw new ValidacaoException("O campo principal é obrigatório!");
    }
}
