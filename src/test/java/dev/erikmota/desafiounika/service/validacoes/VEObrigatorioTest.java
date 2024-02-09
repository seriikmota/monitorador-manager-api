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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VEObrigatorioTest {
    @InjectMocks
    private VEObrigatorio validacao;
    @Mock
    private Endereco e;
    @Mock
    private Monitorador m;

    @Test
    @DisplayName("Retornar sucesso por endereço com todos campos")
    void validarCase01() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(e.getBairro()).willReturn("");
        given(e.getCidade()).willReturn("");
        given(e.getEstado()).willReturn("");
        given(e.getTelefone()).willReturn("");
        given(e.getMonitorador()).willReturn(m);
        given(e.getPrincipal()).willReturn(true);

        assertDoesNotThrow(() -> validacao.validar(e));

    }

    @Test
    @DisplayName("Retornar exception por endereço, sem cep")
    void validarCase02() {
        given(e.getCep()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O CEP é obrigatório!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem endereco")
    void validarCase03() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O Endereço é obrigatório!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem numero")
    void validarCase04() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn(null);

        assertThrows(ValidacaoException.class, () -> validacao.validar(e),
                "O número é obrigatório!");

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O Número é obrigatório!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem bairro")
    void validarCase05() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(e.getBairro()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O Bairro é obrigatório!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem cidade")
    void validarCase06() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(e.getBairro()).willReturn("");
        given(e.getCidade()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("A Cidade é obrigatória!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem estado")
    void validarCase07() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(e.getBairro()).willReturn("");
        given(e.getCidade()).willReturn("");
        given(e.getEstado()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O Estado é obrigatório!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem telefone")
    void validarCase08() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(e.getBairro()).willReturn("");
        given(e.getCidade()).willReturn("");
        given(e.getEstado()).willReturn("");
        given(e.getTelefone()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O Telefone é obrigatório!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem monitorador")
    void validarCase09() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(e.getBairro()).willReturn("");
        given(e.getCidade()).willReturn("");
        given(e.getEstado()).willReturn("");
        given(e.getTelefone()).willReturn("");
        given(e.getMonitorador()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O Monitorador deve ser definido!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por endereço, sem principal")
    void validarCase10() {
        given(e.getCep()).willReturn("");
        given(e.getEndereco()).willReturn("");
        given(e.getNumero()).willReturn("");
        given(e.getBairro()).willReturn("");
        given(e.getCidade()).willReturn("");
        given(e.getEstado()).willReturn("");
        given(e.getTelefone()).willReturn("");
        given(e.getMonitorador()).willReturn(m);
        given(e.getPrincipal()).willReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("O campo Principal é obrigatório!", exception.getMessage());
    }
}