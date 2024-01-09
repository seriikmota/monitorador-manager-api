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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class MonitoradorService {
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

    public List<Monitorador> filtrarTipoPessoa(TipoPessoa tipoPessoa) {
        return repository.findByTipoPessoa(tipoPessoa);
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
        List<Monitorador> monitoradores = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(new File("monitorador.xlsx"));) {
            LocalDate date = LocalDate.of(2020, 11, 25);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            if (!rowIterator.hasNext()) {
                throw new ValidacaoException("O documento está vazio");
            }
            else {
                while (rowIterator.hasNext()) {
                    Monitorador m = new Monitorador();
                    Row row = rowIterator.next();
                    if (row.getRowNum() == 0) row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        int index = cell.getColumnIndex();
                        switch (index) {
                            case 0: m.setTipoPessoa(TipoPessoa.valueOf(cell.getStringCellValue())); break;
                            case 1: m.setCnpj(verifyNull(cell.getStringCellValue())); break;
                            case 2: m.setRazaoSocial(verifyNull(cell.getStringCellValue())); break;
                            case 3: m.setInscricaoEstadual(verifyNull(cell.getStringCellValue())); break;
                            case 4: m.setCpf(verifyNull(cell.getStringCellValue())); break;
                            case 5: m.setNome(verifyNull(cell.getStringCellValue())); break;
                            case 6: m.setRg(verifyNull(cell.getStringCellValue())); break;
                            case 7: m.setData(convertDate(cell.getDateCellValue()));
                            case 8: m.setEmail(verifyNull(cell.toString())); break;
                            case 9: m.setAtivo(cell.getStringCellValue().equals("Sim")); break;
                        }
                    }
                    monitoradores.add(m);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao fazer a leitura do documento");
        }
        if (!monitoradores.isEmpty()){
            monitoradores.forEach(System.out::println);
            monitoradores.forEach(m -> validacoes.forEach(v -> v.validar(m)));
            repository.saveAll(monitoradores);
        }

    }

    private String verifyNull(String stringCellValue) {
        if (stringCellValue.isEmpty())
            return null;
        else
            return stringCellValue;
    }

    private static LocalDate convertDate(Date data) {
        if (data == null)
            throw new ValidacaoException("O campo data é obrigatório!");
        Instant instant = data.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.toLocalDate();
    }
}
