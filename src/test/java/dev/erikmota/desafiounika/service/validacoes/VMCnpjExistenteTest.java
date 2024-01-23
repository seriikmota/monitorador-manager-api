package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.ValidacaoException;
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
    private MonitoradorRepository repository;
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
        given(repository.existsByCnpj(m.getCnpj())).willReturn(false);

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar exception por pessoa juridica, com cnpj cadastrado")
    void validarCase3(){

        given(m.getTipo()).willReturn(TipoPessoa.JURIDICA);
        given(m.getCnpj()).willReturn("");
        given(repository.existsByCnpj(m.getCnpj())).willReturn(true);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Esse cnpj já está cadastrado!", exception.getMessage());
    }
}