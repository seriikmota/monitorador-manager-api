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
class VMCnpjExistenteTest {
    @InjectMocks
    private VMCnpjExistente validacao;
    @Mock
    private MonitoradorRepository monitoradorRepository;
    @Mock
    private Monitorador m;

    @Test
    @DisplayName("Retornar sucesso por pessoa fisica")
    void validarCase1(){
        given(m.getTipo()).willReturn(TipoPessoa.FISICA);

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar sucesso por pessoa juridica, com cnpj não cadastrado")
    void validarCase2(){

        given(m.getTipo()).willReturn(TipoPessoa.JURIDICA);
        given(m.getCnpj()).willReturn("");

        given(monitoradorRepository.existsByCnpj(m.getCnpj())).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar exception por pessoa juridica, com cnpj cadastrado")
    void validarCase3(){

        given(m.getTipo()).willReturn(TipoPessoa.JURIDICA);
        given(m.getCnpj()).willReturn("");
        given(monitoradorRepository.existsByCnpj(m.getCnpj())).willReturn(true);

        given(monitoradorRepository.findByCnpj("")).willReturn(new Monitorador());

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Esse CNPJ já está cadastrado!", exception.getMessage());
    }
}