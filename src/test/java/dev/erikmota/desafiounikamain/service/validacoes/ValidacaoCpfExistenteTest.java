package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacaoCpfExistenteTest {

    @InjectMocks
    private ValidacaoCpfExistente validador;
    @Mock
    private MonitoradorRepository repository;
    @Mock
    private Monitorador monitorador;

    @Test
    public void testValidarComCpfNulo() {
    }

    @Test
    public void testValidarComCpfExistente() {
    }

    @Test
    public void testValidarComCpfValido() {
    }

}