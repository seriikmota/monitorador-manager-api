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

    public List<Monitorador> importar(MultipartFile file, List<IValidacaoMonitorador> validacoes) {
        List<Monitorador> monitoradores = new ArrayList<>();
        int linha = 0, coluna = 0;
        try (XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            if (!rowIterator.hasNext()) {
                throw new ValidacaoException("O documento está vazio!");
            } else {
                rowIterator.next();
                if (!rowIterator.hasNext())
                    throw new ValidacaoException("O documento está vazio!");
            }

            while (rowIterator.hasNext()) {
                linha++;
                Monitorador m = new Monitorador();
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int index = cell.getColumnIndex();

                    switch (index) {
                        case 0:
                            coluna = 1;
                            String tipoPessoa = cell.getStringCellValue().toUpperCase();
                            try {
                                m.setTipo(TipoPessoa.valueOf(tipoPessoa));
                            } catch (Exception e) {
                                throw new ValidacaoException("O tipo da pessoa está incorreto!");
                            }
                            break;
                        case 1:
                            coluna = 2;
                            String cnpj = cell.toString().replaceAll("[^0-9]", "");
                            if (cnpj.length() == 14) m.setCnpj(cnpj);
                            else throw new ValidacaoException("Esse cnpj é inválido!");
                            break;
                        case 2:
                            coluna = 3;
                            m.setRazao(cell.getStringCellValue());
                            break;
                        case 3:
                            coluna = 4;
                            m.setInscricao(formatInscricao(cell.toString()));
                            break;
                        case 4:
                            coluna = 5;
                            String cpf = cell.toString().replaceAll("[^0-9]", "");
                            if (cpf.length() == 11) m.setCpf(cpf);
                            else throw new ValidacaoException("Esse cpf é inválido!");
                            break;
                        case 5:
                            coluna = 6;
                            m.setNome(cell.getStringCellValue());
                            break;
                        case 6:
                            coluna = 7;
                            m.setRg(cell.getStringCellValue());
                            break;
                        case 7:
                            coluna = 8;
                            try {
                                m.setData(convertDate(cell.getDateCellValue()));
                            } catch (IllegalStateException e){
                                throw new ValidacaoException("A data está incorreta!");
                            }
                            break;
                        case 8:
                            coluna = 9;
                            m.setEmail(cell.toString());
                            break;
                        case 9:
                            coluna = 10;
                            m.setAtivo(cell.getStringCellValue().equals("Sim"));
                            break;
                    }
                }
                validacoes.forEach(v -> v.validar(m));
                monitoradores.add(m);
                coluna = 0;
                verificaDuplicados(monitoradores);
            }
            linha = 0;
        } catch (ValidacaoException e) {
            throw new ValidacaoException(e.getMessage() + " Linha: " + linha);
        } catch (Exception e) {
            throw new ValidacaoException("Linha: " + linha + " Coluna: " + coluna);
        }
        return monitoradores;
    }

    public byte[] gerarModelo() {
        String[] colunas = {"Tipo Pessoa", "Cnpj", "Razao Social", "Inscrição Estadual", "Cpf", "Nome", "Rg", "Data", "Email", "Ativo"};
        try (Workbook workbook = new XSSFWorkbook()) {
            CreationHelper createHelper = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Monitorador");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < colunas.length; i++) {
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
            for (int i = 0; i < 10; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new ValidacaoException("Erro ao gerar o modelo para importação de monitoradores!");
        }
    }

    private static LocalDate convertDate(Date data) {
        if (data == null)
            throw new ValidacaoException("O campo data é obrigatório!");
        Instant instant = data.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.toLocalDate();
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

    private static String formatInscricao(String inscricao){
        inscricao = inscricao.replace(".", "");
        int index = inscricao.indexOf('E');
        if (index != -1){
            inscricao = inscricao.substring(0, index);
        }
        return inscricao;
    }
}
