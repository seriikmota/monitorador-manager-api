package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.repository.MonitoradorRepository;
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
class VMCpfExistenteTest {
    @InjectMocks
    private VMCpfExistente validacao;
    @Mock
    private MonitoradorRepository monitoradorRepository;
    @Mock
    private Monitorador m;

    @Test
    @DisplayName("Retornar sucesso por pessoa juridica")
    void validarCase1(){
        given(m.getTipo()).willReturn(TipoPessoa.JURIDICA);

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar sucesso por pessoa fisica, com cpf não cadastrado")
    void validarCase2(){

        given(m.getTipo()).willReturn(TipoPessoa.FISICA);
        given(m.getCpf()).willReturn("");
        given(monitoradorRepository.existsByCpf(m.getCpf())).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar exception por pessoa fisica, com cpf cadastrado")
    void validarCase3(){

        given(m.getTipo()).willReturn(TipoPessoa.FISICA);
        given(m.getCpf()).willReturn("");
        given(monitoradorRepository.existsByCpf(m.getCpf())).willReturn(true);

        given(monitoradorRepository.findByCpf("")).willReturn(new Monitorador());

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Esse CPF já está cadastrado!", exception.getMessage());
    }

}