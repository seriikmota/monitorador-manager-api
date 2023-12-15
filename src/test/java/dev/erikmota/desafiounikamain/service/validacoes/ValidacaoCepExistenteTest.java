package dev.erikmota.desafiounikamain.service.validacoes;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ValidacaoCepExistenteTest {

    @InjectMocks
    private ValidacaoEnderecoPrincipal validador;
    @Mock
    private EnderecoRepository repository;
    @Mock
    private Endereco endereco;

    @Test
    void test(){
        List<Endereco> enderecos = null;
        BDDMockito.given(repository.findAll()).willReturn(enderecos);

    }
}