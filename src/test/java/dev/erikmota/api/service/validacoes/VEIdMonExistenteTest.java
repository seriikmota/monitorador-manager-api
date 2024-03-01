package dev.erikmota.api.service.validacoes;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.models.Monitorador;
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
class VEIdMonExistenteTest {
    @InjectMocks
    private VEIdMonExistente validacao;
    @Mock
    private Endereco e;
    @Mock
    private Monitorador m;
    @Mock
    private MonitoradorRepository monitoradorRepository;

    @Test
    @DisplayName("Retornar sucesso para monitorador existente")
    void validarCase1(){
        given(e.getMonitorador()).willReturn(m);
        given(monitoradorRepository.existsById(m.getId())).willReturn(true);

        assertDoesNotThrow(() -> validacao.validar(e));
    }

    @Test
    @DisplayName("Retornar exception para monitorador não existente")
    void validarCase2(){
        given(e.getMonitorador()).willReturn(m);
        given(monitoradorRepository.existsById(m.getId())).willReturn(false);


        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("Esse monitorador não foi encontrado!", exception.getMessage());

    }

}