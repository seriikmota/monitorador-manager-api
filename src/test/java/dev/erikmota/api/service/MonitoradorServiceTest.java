package dev.erikmota.api.service;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonitoradorServiceTest {

    @InjectMocks
    private MonitoradorService service;
    @Mock
    private EnderecoService enderecoService;
    @Mock
    private MonitoradorRepository monitoradorRepository;
    @Spy
    private List<IVMonitorador> validacoes = new ArrayList<>();
    @Mock
    private PoiService poiService;
    @Mock
    private JasperService jasperService;
    @Mock
    private Endereco e;
    private Monitorador m;

    @BeforeEach
    public void setUp(){
        m = new Monitorador(null, TipoPessoa.FISICA, "143.182.340-61", "Gabriel", "11.010.161-3", "89.533.297/0001-64", "Julia e Emanuel Buffet ME", "081.933.457.927", "valentina_aragao@racml.com.br", LocalDate.of(2000,1,1), true, new ArrayList<>());
        adicionarValidacoes();
    }

    @Test
    void cadastrarCase1() {
        validacoes.forEach(item -> doNothing().when(item).validar(m));

        assertDoesNotThrow(() -> service.cadastrar(m));
        verify(monitoradorRepository, times(1)).save(m);
    }

    @Test
    void cadastrarCase2() {
        validacoes.forEach(item -> doNothing().when(item).validar(m));
        m.setEnderecos(List.of(e));

        assertDoesNotThrow(() -> service.cadastrar(m));
        verify(monitoradorRepository, times(1)).save(m);
        verify(enderecoService, times(1)).cadastrar(e, m.getId());
    }

    @Test
    void cadastrarCase3() {
        validacoes.forEach(item -> lenient().doThrow(ValidacaoException.class).when(item).validar(m));

        assertThrows(ValidacaoException.class, () -> service.cadastrar(m));
        verify(monitoradorRepository, never()).save(m);
    }

    @Test
    void editarCase1() {
        when(monitoradorRepository.existsById(anyLong())).thenReturn(true);
        validacoes.forEach(item -> doNothing().when(item).validar(m));
        when(monitoradorRepository.getReferenceById(anyLong())).thenReturn(m);

        assertDoesNotThrow(() -> service.editar(m, anyLong()));
    }

    @Test
    void editarCase2() {
        when(monitoradorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> service.editar(m, anyLong()));
    }

    @Test
    void excluirCase1() {
        when(monitoradorRepository.existsById(anyLong())).thenReturn(true);
        when(monitoradorRepository.getReferenceById(anyLong())).thenReturn(m);

        service.excluir(anyLong());
        verify(monitoradorRepository, times(1)).delete(m);
    }

    @Test
    void excluirCase2() {
        when(monitoradorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> service.editar(m, anyLong()));
        verify(monitoradorRepository, never()).delete(m);
    }

    @Test
    void listarCase1() {
        service.listar();

        then(monitoradorRepository).should().findAll();
    }

    @Test
    void filtrarCase1() {
        service.filtrar(any(), any(), any());

        then(monitoradorRepository).should().filtrar(any(), any(), any());
    }

    @Test
    void gerarModeloCase1() {
        service.gerarModelo();

        then(poiService).should().gerarModelo();
    }

    @Test
    void importarCase1() {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        List<Monitorador> monitoradores = Arrays.asList(m, m);
        when(poiService.importar(file, validacoes)).thenReturn(monitoradores);

        service.importar(file);

        verify(monitoradorRepository, atLeastOnce()).save(m);
    }

    @Test
    void importarCase2() {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        service.importar(file);

        verify(monitoradorRepository, never()).save(m);
    }

    @Test
    void gerarRelatorioPdfCase1() {
        service.gerarRelatorioPdf(null, any(), any(), any());

        verify(jasperService, times(1)).gerarPdfGeral(any(), any(), any());
    }

    @Test
    void gerarRelatorioPdfCase2() {
        service.gerarRelatorioPdf(anyLong(), null, null, null);

        verify(jasperService, times(1)).gerarPdfInd(anyLong());
    }

    @Test
    void gerarRelatorioExcelCase1() {
        var lista = Arrays.asList(m, m);
        when(monitoradorRepository.filtrar(any(), any(), any())).thenReturn(lista);

        service.gerarRelatorioExcel(null, any(), any(), any());

        verify(poiService, times(1)).exportarMonitorador(lista);
    }

    @Test
    void gerarRelatorioExcelCase2() {
        when(monitoradorRepository.getReferenceById(anyLong())).thenReturn(m);

        service.gerarRelatorioExcel(anyLong(), null, null, null);

        verify(poiService, times(1)).exportarMonitorador(List.of(m));
    }

    void adicionarValidacoes(){
        VMCpfCnpj v1 = mock(VMCpfCnpj.class);
        VMPessoaFisica v2 = mock(VMPessoaFisica.class);
        VMCnpjExistente v3 = mock(VMCnpjExistente.class);
        VMCpfExistente v4 = mock(VMCpfExistente.class);
        VMObrigatorio v5 = mock(VMObrigatorio.class);
        VMPessoaJuridica v6 = mock(VMPessoaJuridica.class);

        validacoes.addAll(Arrays.asList(v1, v2, v3, v4, v5, v6));
    }
}