package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.repository.EnderecoRepository;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VEEnderecoTest {
    @InjectMocks
    private VEEndereco validacao;
    @Mock
    private Endereco e;
    @Mock
    private EnderecoRepository repository;

    @Test
    @DisplayName("Retornar sucesso cep não existir no banco")
    void validarCase1() {
        given(e.getCep()).willReturn("");
        given(repository.existsByCep(anyString())).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(e));
    }

    @Test
    @DisplayName("Retornar sucesso cep existir no banco, mas não ter endereço igual")
    void validarCase2() {
        given(e.getCep()).willReturn("");
        given(repository.existsByCep(anyString())).willReturn(true);
        given(e.getEndereco()).willReturn("");
        given(repository.existsByEndereco(anyString())).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(e));
    }

    @Test
    @DisplayName("Retornar sucesso cep existir no banco e ter endereço igual")
    void validarCase3() {
        given(e.getCep()).willReturn("");
        given(repository.existsByCep(e.getCep())).willReturn(true);
        given(e.getEndereco()).willReturn("");
        given(repository.existsByEndereco(e.getEndereco())).willReturn(true);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O campo endereço já existe, especifique mais!", exception.getMessage());
    }
}