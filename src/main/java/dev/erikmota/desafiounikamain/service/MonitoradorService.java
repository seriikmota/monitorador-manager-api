package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.validacoes.IValidacaoMonitorador;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.tags.EditorAwareTag;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public void importar(MultipartFile file){
        System.out.println(file.getName());
    }
}
