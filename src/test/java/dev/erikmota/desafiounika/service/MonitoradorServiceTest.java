package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import dev.erikmota.desafiounika.service.validacoes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private MonitoradorDAO monitoradorDAO;
    @Spy
    private List<IVMonitorador> validacoes = new ArrayList<>();
    @Mock
    private PoiService poiService;
    @Mock
    private JasperService jasperService;
    private Monitorador m;

    @BeforeEach
    public void setUp(){
        m = new Monitorador(null, TipoPessoa.FISICA, "143.182.340-61", "Gabriel", "11.010.161-3", "89.533.297/0001-64", "Julia e Emanuel Buffet ME", "081.933.457.927", "valentina_aragao@racml.com.br", LocalDate.of(2000,1,1), true, null);
        adicionarValidacoes();
    }

    @Test
    void cadastrarCase1() {
        validacoes.forEach(item -> doNothing().when(item).validar(m));

        assertDoesNotThrow(() -> service.cadastrar(m));
        verify(monitoradorDAO, times(1)).save(m);
    }

    @Test
    void cadastrarCase2() {
        validacoes.forEach(item -> lenient().doThrow(ValidacaoException.class).when(item).validar(m));

        assertThrows(ValidacaoException.class, () -> service.cadastrar(m));
        verify(monitoradorDAO, never()).save(m);
    }

    @Test
    void editarCase1() {
        when(monitoradorDAO.existsById(anyLong())).thenReturn(true);
        validacoes.forEach(item -> doNothing().when(item).validar(m));

        assertDoesNotThrow(() -> service.editar(m, anyLong()));
        verify(monitoradorDAO, times(1)).edit(m);
    }

    @Test
    void editarCase2() {
        when(monitoradorDAO.existsById(anyLong())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> service.editar(m, anyLong()));
        verify(monitoradorDAO, never()).edit(m);
    }

    @Test
    void excluirCase1() {
        when(monitoradorDAO.existsById(anyLong())).thenReturn(true);
        when(monitoradorDAO.findById(anyLong())).thenReturn(m);

        service.excluir(anyLong());
        verify(monitoradorDAO, times(1)).delete(m);
    }

    @Test
    void excluirCase2() {
        when(monitoradorDAO.existsById(anyLong())).thenReturn(false);

        assertThrows(ValidacaoException.class, () -> service.editar(m, anyLong()));
        verify(monitoradorDAO, never()).delete(m);
    }

    @Test
    void listarCase1() {
        service.listar();

        then(monitoradorDAO).should().findAll();
    }

    @Test
    void filtrarCase1() {
        service.filtrar(any(), any(), any());

        then(monitoradorDAO).should().filter(any(), any(), any());
    }

    @Test
    void gerarModeloCase1() {
        service.gerarModelo();

        then(poiService).should().gerarModelo();
    }

    @Test
    void importarCase1() {
    }

    @Test
    void importarCase2() {
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
        when(monitoradorDAO.filter(any(), any(), any())).thenReturn(lista);

        service.gerarRelatorioExcel(null, any(), any(), any());

        verify(poiService, times(1)).exportarMonitorador(lista);
    }

    @Test
    void gerarRelatorioExcelCase2() {
        when(monitoradorDAO.findById(anyLong())).thenReturn(m);

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