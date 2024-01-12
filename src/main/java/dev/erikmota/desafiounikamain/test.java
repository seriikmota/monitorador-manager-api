package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        CPFValidator validator = new CPFValidator();
        validator.initialize(null);
        if (!validator.isValid("", null))
            throw new ValidacaoException("Esse cpf é inválido!");
    }
}
