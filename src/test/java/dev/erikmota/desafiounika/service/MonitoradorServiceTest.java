package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.validacoes.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoradorServiceTest {
    @InjectMocks
    private MonitoradorService service;
    @Mock
    private Monitorador m;
    @Mock
    private Endereco e;
    @Mock
    private MonitoradorRepository repository;
    @Mock
    private MonitoradorDAO monitoradorDAO;
    @Mock
    private List<IVMonitorador> validacoesCad;

    @Test
    @DisplayName("Retornar sucesso por salvar no repository")
    void cadastrarCase1() {
        service.cadastrar(m);

        then(repository).should().save(m);
    }


    @Test
    @DisplayName("Retornar sucesso para editar monitorador")
    void editarCase1() {
        given(repository.getReferenceById(1L)).willReturn(m);

        service.editar(1L, mock(Monitorador.class));

        verify(m).editar(any());
        verify(repository).getReferenceById(1L);
    }

    @Test
    @DisplayName("Retornar sucesso para excluir monitorador")
    void excluirCase1() {
        given(repository.existsById(anyLong())).willReturn(true);
        given(repository.getReferenceById(anyLong())).willReturn(m);
        given(m.getEnderecos()).willReturn(Collections.emptyList());

        service.excluir(1L);

        then(repository).should().delete(m);
    }

    @Test
    @DisplayName("Retornar exception para excluir monitorador inexistente")
    void excluirCase2() {
        given(repository.existsById(anyLong())).willReturn(false);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> service.excluir(1L));
        assertEquals("Esse monitorador não está cadastrado!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception para excluir monitorador com endereço cadastrado")
    void excluirCase3() {
        given(repository.existsById(anyLong())).willReturn(true);
        given(repository.getReferenceById(anyLong())).willReturn(m);
        given(m.getEnderecos()).willReturn(Collections.singletonList(new Endereco()));

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> service.excluir(1L));
        assertEquals("Não é possivel excluir um monitorador com endereços cadastrados!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar sucesso para listar monitoradores")
    void listarCase1() {
        service.listar();

        then(repository).should().findAll();
    }

    @Test
    void filtrarCase1(){
        String text = "Nome";
        Boolean ativo = true;
        TipoPessoa tipoPessoa = TipoPessoa.FISICA;

        List<Monitorador> resultadoEsperado = Arrays.asList(new Monitorador(), new Monitorador());
        when(monitoradorDAO.filter(text, ativo, tipoPessoa)).thenReturn(resultadoEsperado);

        List<Monitorador> resultadoAtual = service.filtrar(text, ativo, tipoPessoa);

        verify(monitoradorDAO).filter(text, ativo, tipoPessoa);
        assertEquals(resultadoEsperado, resultadoAtual);
    }
}