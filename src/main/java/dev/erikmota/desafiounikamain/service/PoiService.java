package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.validacoes.IValidacaoMonitorador;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class PoiService {

    public byte[] gerarModelo() {
        String[] colunas = {"Tipo Pessoa", "CNPJ", "Razao Social", "Inscrição Estadual", "CPF", "Nome", "RG", "Data", "Email", "Ativo"};
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monitorador");
            Row headerRow = sheet.createRow(0);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            for (int i = 0; i < colunas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(colunas[i]);
                cell.setCellStyle(headerCellStyle);
                sheet.setColumnWidth(i, 18 * 256);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ValidacaoException("Erro ao gerar o modelo para importação de monitoradores!");
        }
    }

    public List<Monitorador> importar(MultipartFile file, List<IValidacaoMonitorador> validacoes) {
        List<Monitorador> monitoradores = new ArrayList<>();
        int linha = 0, coluna = 0;
        try (XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream())) {
            Iterator<Row> rowIterator = wb.getSheetAt(0).rowIterator();

            if (!rowIterator.hasNext()) {
                throw new ValidacaoException("O documento está vazio!");
            } else {
                rowIterator.next();
                linha++;
                if (!rowIterator.hasNext())
                    throw new ValidacaoException("O documento está vazio!");
            }
            while (rowIterator.hasNext()) {
                linha++;
                Monitorador m = new Monitorador();
                Row row = rowIterator.next();
                for (Cell cell : row) {
                    int index = cell.getColumnIndex();
                    coluna = index + 1;

                    switch (index) {
                        case 0 -> m.setTipo(TipoPessoa.valueOf(cell.getStringCellValue().toUpperCase()));
                        case 1 -> {
                            String cnpj = obterValor(cell);
                            m.setCnpj(cnpj != null && cnpj.replaceAll("[^0-9]", "").length() == 14 ? cnpj : null);
                        }
                        case 2 -> m.setRazao(cell.getStringCellValue());
                        case 3 -> m.setInscricao(obterValor(cell));
                        case 4 -> {
                            String cpf = obterValor(cell);
                            m.setCpf(cpf != null && cpf.replaceAll("[^0-9]", "").length() == 11 ? cpf : null);
                        }
                        case 5 -> m.setNome(cell.getStringCellValue());
                        case 6 -> m.setRg(obterValor(cell));
                        case 7 -> m.setData(converteData(cell.getDateCellValue()));
                        case 8 -> m.setEmail(cell.getStringCellValue());
                        case 9 -> m.setAtivo(cell.getStringCellValue().equalsIgnoreCase("Sim"));
                    }
                }
                validacoes.forEach(v -> v.validar(m));
                monitoradores.add(m);
                coluna = 0;
                verificaDuplicados(monitoradores);
            }
        } catch (ValidacaoException e) {
            throw new ValidacaoException(e.getMessage() + " Linha: " + linha);
        } catch (Exception e) {
            throw new ValidacaoException("Linha: " + linha + " Coluna: " + coluna);
        }
        return monitoradores;
    }

    private static LocalDate converteData(Date data) {
        if (data == null)
            throw new ValidacaoException("O campo data é obrigatório!");

        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static void verificaDuplicados(List<Monitorador> monitoradores){
        for (int i = monitoradores.size()-1; i >= 1; i--){
            for (int j = i-1; j >= 0; j--){
                if (monitoradores.get(i).getCpf() != null && Objects.equals(monitoradores.get(i).getCpf(), monitoradores.get(j).getCpf()))
                    throw new ValidacaoException("Esse cpf já foi digitado!");
                if (monitoradores.get(i).getCnpj() != null && Objects.equals(monitoradores.get(i).getCnpj(), monitoradores.get(j).getCnpj()))
                    throw new ValidacaoException("Esse cnpj já foi digitado!");
            }
        }
    }

    private static String obterValor(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.format("%.0f", cell.getNumericCellValue());
            default -> null;
        };
    }


}
