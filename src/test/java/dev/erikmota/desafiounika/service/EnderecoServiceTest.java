package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.EnderecoDAO;
import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import dev.erikmota.desafiounika.service.validacoes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {
    @InjectMocks
    private EnderecoService service;
    @Mock
    private EnderecoDAO enderecoDAO;
    @Mock
    private MonitoradorDAO monitoradorDAO;
    @Mock
    private ViaCepService viaCepService;
    @Mock
    private JasperService jasperService;
    @Mock
    private PoiService poiService;
    @Mock
    private Endereco e;
    @Mock
    private Monitorador m;
    @Spy
    private List<IVEndereco> validacoes = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        adicionarValidacoes();
    }

    @Test
    void cadastrarCase1() {
        when(monitoradorDAO.findById(anyLong())).thenReturn(m);
        validacoes.forEach(item -> doNothing().when(item).validar(e));

        assertDoesNotThrow(() -> service.cadastrar(e, m.getId()));
        verify(enderecoDAO, times(1)).save(e, m.getId());
    }

    @Test
    void cadastrarCase2() {
        when(monitoradorDAO.findById(anyLong())).thenReturn(m);
        validacoes.forEach(item -> lenient().doThrow(ValidacaoException.class).when(item).validar(e));

        assertThrows(ValidacaoException.class, () -> service.cadastrar(e, m.getId()));
        verify(enderecoDAO, never()).save(e, m.getId());
    }

    @Test
    void editarCase1() {
        when(monitoradorDAO.findById(1L)).thenReturn(m);
        validacoes.forEach(item -> doNothing().when(item).validar(e));

        assertDoesNotThrow(() -> service.editar(e, anyLong(), 1L));
        verify(enderecoDAO, times(1)).edit(e);
    }

    @Test
    void editarCase2() {
        when(monitoradorDAO.findById(1L)).thenReturn(m);
        validacoes.forEach(item -> lenient().doThrow(ValidacaoException.class).when(item).validar(e));

        assertThrows(ValidacaoException.class, () -> service.editar(e, anyLong(), 1L));
        verify(enderecoDAO, never()).edit(e);
    }

    @Test
    void excluirCase1() {
        when(enderecoDAO.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> service.excluir(1L));
        verify(enderecoDAO, times(1)).delete(1L);
    }

    @Test
    void excluirCase2() {
        when(enderecoDAO.existsById(anyLong())).thenReturn(false);

        assertDoesNotThrow(() -> service.excluir(1L));
        verify(enderecoDAO, never()).delete(1L);
    }

    @Test
    void listarCase1() {
        service.listar();

        verify(enderecoDAO, times(1)).findAll();
    }

    @Test
    void filtrarCase1() {
        service.filtrar(any(), any(), any(), anyLong());

        verify(enderecoDAO, times(1)).filter(any(), any(), any(), anyLong());
    }

    @Test
    void gerarRelatorioPdfCase1() {
        service.gerarRelatorioPdf(anyLong(), any(), any(), any(), anyLong());

        verify(jasperService, times(1)).gerarPdfEndereco(anyLong(), any(), any(), any(), anyLong());
    }

    @Test
    void gerarRelatorioExcelCase1() {
        var lista = Arrays.asList(e, e);
        when(enderecoDAO.filter("A", "A", "A", 1L)).thenReturn(lista);
        when(monitoradorDAO.findById(1L)).thenReturn(m);

        service.gerarRelatorioExcel(null, "A", "A", "A", 1L);

        verify(poiService, times(1)).exportarEndereco(lista);
    }

    @Test
    void gerarRelatorioExcelCase2() {
        when(enderecoDAO.findById(1L)).thenReturn(e);
        when(monitoradorDAO.findById(2L)).thenReturn(m);

        service.gerarRelatorioExcel(1L, null, null, null, 2L);

        verify(poiService, times(1)).exportarEndereco(List.of(e));
    }

    @Test
    void buscarCepCase1() {
        service.buscarCep(anyString());

        verify(viaCepService, times(1)).buscarCep(anyString());
    }

    void adicionarValidacoes(){
        VEEndereco v1 = mock(VEEndereco.class);
        VEIdMonExistente v2 = mock(VEIdMonExistente.class);
        VEObrigatorio v3 = mock(VEObrigatorio.class);
        VEPrincipal v4 = mock(VEPrincipal.class);
        VETelefone v5 = mock(VETelefone.class);

        validacoes.addAll(Arrays.asList(v1, v2, v3, v4, v5));
    }
}