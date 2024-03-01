package dev.erikmota.api.service;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.repository.EnderecoRepository;
import dev.erikmota.api.repository.MonitoradorRepository;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import dev.erikmota.api.service.validacoes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {
    @InjectMocks
    private EnderecoService service;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private MonitoradorRepository monitoradorRepository;
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
        when(monitoradorRepository.getReferenceById(anyLong())).thenReturn(m);
        validacoes.forEach(item -> doNothing().when(item).validar(e));

        assertDoesNotThrow(() -> service.cadastrar(e, m.getId()));
        verify(enderecoRepository, times(1)).save(e);
    }

    @Test
    void cadastrarCase2() {
        when(monitoradorRepository.getReferenceById(anyLong())).thenReturn(m);
        validacoes.forEach(item -> lenient().doThrow(ValidacaoException.class).when(item).validar(e));

        assertThrows(ValidacaoException.class, () -> service.cadastrar(e, m.getId()));
        verify(enderecoRepository, never()).save(e);
    }

    @Test
    void editarCase1() {
        when(enderecoRepository.existsById(1L)).thenReturn(true);
        when(monitoradorRepository.existsById(2L)).thenReturn(true);
        when(enderecoRepository.getReferenceById(1L)).thenReturn(e);
        when(monitoradorRepository.getReferenceById(2L)).thenReturn(m);
        validacoes.forEach(item -> doNothing().when(item).validar(e));

        assertDoesNotThrow(() -> service.editar(e, 1L, 2L));
    }

    @Test
    void editarCase2() {
        when(enderecoRepository.existsById(anyLong())).thenReturn(true);
        when(monitoradorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> service.editar(e, anyLong(), 1L));
    }

    @Test
    void editarCase3() {
        when(enderecoRepository.existsById(anyLong())).thenReturn(false);
        validacoes.forEach(item -> lenient().doThrow(ValidacaoException.class).when(item).validar(e));

        assertThrows(ValidacaoException.class, () -> service.editar(e, anyLong(), 1L));
    }

    @Test
    void excluirCase1() {
        when(enderecoRepository.existsById(anyLong())).thenReturn(true);
        when(enderecoRepository.getReferenceById(anyLong())).thenReturn(e);

        assertDoesNotThrow(() -> service.excluir(anyLong()));
        verify(enderecoRepository, times(1)).delete(e);
    }

    @Test
    void excluirCase2() {
        when(enderecoRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> service.excluir(1L));
        verify(enderecoRepository, never()).delete(e);
    }

    @Test
    void listarCase1() {
        service.listar();

        verify(enderecoRepository, times(1)).findAll();
    }

    @Test
    void filtrarCase1() {
        service.filtrar(any(), any(), any(), anyLong());

        verify(enderecoRepository, times(1)).filtrar(any(), any(), any(), anyLong());
    }

    @Test
    void gerarRelatorioPdfCase1() {
        service.gerarRelatorioPdf(anyLong(), any(), any(), any(), anyLong());

        verify(jasperService, times(1)).gerarPdfEndereco(anyLong(), any(), any(), any(), anyLong());
    }

    @Test
    void gerarRelatorioExcelCase1() {
        var lista = Arrays.asList(e, e);
        when(enderecoRepository.filtrar("A", "A", "A", 1L)).thenReturn(lista);
        when(monitoradorRepository.getReferenceById(1L)).thenReturn(m);

        service.gerarRelatorioExcel(null, "A", "A", "A", 1L);

        verify(poiService, times(1)).exportarEndereco(lista);
    }

    @Test
    void gerarRelatorioExcelCase2() {
        when(enderecoRepository.getReferenceById(1L)).thenReturn(e);
        when(monitoradorRepository.getReferenceById(2L)).thenReturn(m);

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