package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.repository.EnderecoRepository;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
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
    @DisplayName("Retornar sucesso se o endereço e número não existir no banco")
    void validarCase1() {
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(repository.existsByEnderecoAndNumero(anyString(), anyString())).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(e));
    }

    @Test
    @DisplayName("Retornar exception caso endereço e número já estejam cadastrados")
    void validarCase2() {
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(repository.existsByEnderecoAndNumero(anyString(), anyString())).willReturn(true);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("Esse endereço já existe, altere o campo endereço e/ou número!", exception.getMessage());
    }
}