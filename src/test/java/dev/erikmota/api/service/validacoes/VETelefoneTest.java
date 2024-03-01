package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VETelefoneTest {
    @InjectMocks
    private VETelefone validacao;
    @Mock
    private Endereco e;

    @Test
    @DisplayName("Returnar sucesso para telefone válido")
    void validarCase1(){
        given(e.getTelefone()).willReturn("62984291788");

        assertDoesNotThrow(() -> validacao.validar(e));
    }

    @Test
    @DisplayName("Returnar exception para telefone inválido")
    void validarCase2(){
        given(e.getTelefone()).willReturn("");

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("Digite um número de telefone válido!", exception.getMessage());
    }
}