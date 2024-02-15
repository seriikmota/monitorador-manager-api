package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VEPrincipalTest {
    @InjectMocks
    private VEPrincipal validacao;
    @Mock
    private Endereco e, e2;
    @Mock
    private Monitorador m;

    @Test
    @DisplayName("Retornar sucesso monitorador não possui endereço principal")
    void validarCase1() {
        List<Endereco> enderecoList = Arrays.asList(e, e2);

        when(e.getMonitorador()).thenReturn(m);
        when(m.getEnderecos()).thenReturn(enderecoList);
        when(e.getCep()).thenReturn("1");
        when(e.getPrincipal()).thenReturn(true);
        when(e2.getPrincipal()).thenReturn(false);

        assertDoesNotThrow(() -> validacao.validar(e));
    }

    @Test
    @DisplayName("Retornar exception monitorador possui endereço principal")
    void validarCase2() {
        List<Endereco> enderecoList = Arrays.asList(e, e2);

        when(e.getMonitorador()).thenReturn(m);
        when(m.getEnderecos()).thenReturn(enderecoList);
        when(e.getCep()).thenReturn("1");
        when(e.getPrincipal()).thenReturn(true);
        when(e2.getPrincipal()).thenReturn(true);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("Esse monitorador já possui endereço principal!", exception.getMessage());
    }

}