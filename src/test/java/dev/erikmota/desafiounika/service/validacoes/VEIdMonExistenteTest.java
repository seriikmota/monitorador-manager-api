package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
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
class VEIdMonExistenteTest {
    @InjectMocks
    private VEIdMonExistente validacao;
    @Mock
    private Endereco e;
    @Mock
    private Monitorador m;
    @Mock
    private MonitoradorRepository repository;

    @Test
    @DisplayName("Retornar sucesso para monitorador existente")
    void validarCase1(){
        given(e.getMonitorador()).willReturn(m);
        given(repository.existsById(m.getId())).willReturn(true);

        assertDoesNotThrow(() -> validacao.validar(e));
    }

    @Test
    @DisplayName("Retornar exception para monitorador não existente")
    void validarCase2(){
        given(e.getMonitorador()).willReturn(m);
        given(repository.existsById(m.getId())).willReturn(false);


        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(e));
        assertEquals("Esse monitorador não existe!", exception.getMessage());

    }

}