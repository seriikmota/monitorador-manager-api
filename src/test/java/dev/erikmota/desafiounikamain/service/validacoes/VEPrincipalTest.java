package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VEPrincipalTest {
    @InjectMocks
    private VEPrincipal validacao;
    @Mock
    private Endereco e;
    @Mock
    private Monitorador m;

    @Test
    @DisplayName("Retornar sucesso monitorador não possui endereço principal")
    void validarCase1(){
        List<Endereco> enderecoList = Arrays.asList(e, e);
        given(e.getMonitorador()).willReturn(m);
        given(m.getEnderecos()).willReturn(enderecoList);
        given(e.getPrincipal()).willReturn(true);
        given(enderecoList.get(0).getPrincipal()).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(e));
    }
    @Test
    @DisplayName("Retornar exception monitorador possui endereço principal")
    void validarCase2(){
        List<Endereco> enderecoList = Arrays.asList(e, e);
        given(e.getMonitorador()).willReturn(m);
        given(m.getEnderecos()).willReturn(enderecoList);
        given(e.getPrincipal()).willReturn(true);
        given(enderecoList.get(0).getPrincipal()).willReturn(true);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("Esse monitorador já possui endereço principal!", exception.getMessage());
    }

}