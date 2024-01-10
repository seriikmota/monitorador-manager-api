package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.validacoes.IValidacaoMonitorador;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class MonitoradorService {
    private final PoiService poiService = new PoiService();
    private final JasperService jasperService = new JasperService();
    @Autowired
    private MonitoradorRepository repository;
    @Autowired
    private List<IValidacaoMonitorador> validacoes;

    public void cadastrar(Monitorador m){
        validacoes.forEach(v -> v.validar(m));
        repository.save(m);
    }

    public void editar(Long id, Monitorador m){

        Monitorador novoMonitorador = repository.getReferenceById(id);
        novoMonitorador.editar(m);
    }

    public List<Monitorador> listar(){
        return repository.findAll();
    }

    public void excluir(Long id){
        if (repository.existsById(id))
            repository.delete(repository.getReferenceById(id));
        else
            throw new ValidacaoException("Esse monitorador não está cadastrado");
    }

    public List<Monitorador> filtrarNome(String nome) {
        return repository.findByNomeContains(nome);
    }

    public List<Monitorador> filtrarCpf(String cpf) {
        return repository.findByCpfContains(cpf);
    }

    public List<Monitorador> filtrarCnpj(String cnpj) {
        return repository.findByCnpjContains(cnpj);
    }

    public List<Monitorador> filtrarAtivo(Boolean ativo) {
        return repository.findByAtivo(ativo);
    }

    public List<Monitorador> filtrarTipoPessoa(TipoPessoa tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Monitorador> filtrar(String text, Boolean ativo, TipoPessoa tipoPessoa) {
        return repository.filtrar(text, ativo, tipoPessoa);
    }

    public void modeloImportar(){
        String[] colunas = {"Tipo Pessoa", "Cnpj", "Razao Social", "Inscrição Estadual", "Cpf", "Nome", "Rg", "Data", "Email", "Ativo"};
        try(Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream("Monitoradores.xlsx"))
        {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Monitorador");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            for(int i = 0; i < colunas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerCellStyle);
            }
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("FISICA ou JURIDICA");
            row.createCell(1).setCellValue("XX.XXX.XXX/0001-XX");
            row.createCell(2).setCellValue("Razao Social");
            row.createCell(3).setCellValue("Inscrição estadual");
            row.createCell(4).setCellValue("XXX.XXX.XXX-XX");
            row.createCell(5).setCellValue("Nome");
            row.createCell(6).setCellValue("Rg");
            row.createCell(7).setCellValue("dd/MM/yyyy");
            row.createCell(8).setCellValue("example@example.com");
            row.createCell(9).setCellValue("Sim ou Não");
            for(int i = 0; i < 10; i++){
                sheet.autoSizeColumn(i);
            }
            workbook.write(fileOut);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void importar() {
        List<Monitorador> monitoradores = poiService.importar();
        if (!monitoradores.isEmpty()){
            monitoradores.forEach(System.out::println);
            monitoradores.forEach(m -> validacoes.forEach(v -> v.validar(m)));
            repository.saveAll(monitoradores);
        }

    }

    public void exportar(){
        poiService.exportar();
    }

    public void gerarRelatorioAll(){
        jasperService.gerarPdf(repository.findAll());
    }

    public void gerarRelatorio(Long id){
        List<Monitorador> m = new ArrayList<>();
        m.add(repository.getReferenceById(id));
        jasperService.gerarPdf(m);
    }

}
