package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class VMPessoaFisicaTest {

    @InjectMocks
    private VMPessoaFisica validador;

    @Mock
    private Monitorador monitorador;

    @Test
    public void testValidacaoCnpjCpfCpfNullAndCnpjNull() {
    }

    @Test
    public void testValidacaoCnpjCpfPessoaFisicaNomeNull() {
    }

    @Test
    public void testValidacaoCnpjCpfPessoaJuridicaRazaoSocialNull() {
    }
}
