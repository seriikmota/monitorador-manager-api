package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VECepExistenteTest {
    @InjectMocks
    private VECepExistente validador;
    @Mock
    private EnderecoRepository repository;
    @Mock
    private Endereco endereco;

    @Test
    void returnCepExistente() {
        given(repository.existsByCep(endereco.getCep())).willReturn(true);
        assertThrows(ValidacaoException.class, () -> validador.validar(endereco));
    }
    @Test
    void returnCepNaoExistente() {
        given(repository.existsByCep(endereco.getCep())).willReturn(false);
        assertDoesNotThrow(() -> validador.validar(endereco));
    }

}