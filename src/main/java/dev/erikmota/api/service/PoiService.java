package dev.erikmota.api.service;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.service.exceptions.PoiException;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import dev.erikmota.api.service.validacoes.IVMonitorador;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class PoiService {
    public byte[] gerarModelo() {
        String[] colunas = {"Tipo Pessoa", "CNPJ", "Razão Social", "Inscrição Estadual", "CPF", "Nome", "RG", "Data", "Email", "Status"};
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monitorador");
            configExportExcel(workbook, sheet, colunas);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            throw new PoiException("Ocorreu um erro ao gerar o modelo para importação de monitoradores!");
        }
    }

    public byte[] exportarMonitorador(List<Monitorador> monitoradorList) {
        if (monitoradorList.isEmpty())
            throw new PoiException("Não é possível gerar relatorio sem monitoradores!");
        String[] colunas = {"Código", "Tipo Pessoa", "CNPJ", "Razão Social", "Inscrição Estadual", "CPF", "Nome", "RG", "Data", "Email", "Status"};
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monitorador");
            configExportExcel(workbook, sheet, colunas);

            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateCellStyle = workbook.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            int linha = 1;

            for (Monitorador m : monitoradorList) {
                Row row = sheet.createRow(linha++);
                row.createCell(0).setCellValue(m.getId());
                row.createCell(1).setCellValue(String.valueOf(m.getTipo()));
                row.createCell(2).setCellValue(m.getCnpj());
                row.createCell(3).setCellValue(m.getRazao());
                row.createCell(4).setCellValue(m.getInscricao());
                row.createCell(5).setCellValue(m.getCpf());
                row.createCell(6).setCellValue(m.getNome());
                row.createCell(7).setCellValue(m.getRg());
                Cell cell = row.createCell(8);
                cell.setCellValue(m.getData());
                cell.setCellStyle(dateCellStyle);
                row.createCell(9).setCellValue(m.getEmail());
                row.createCell(10).setCellValue(m.getAtivo() ? "Ativo" : "Inativo");
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new PoiException("Ocorreu um erro ao gerar o relatorio de monitoradores!");
        }
    }

    public byte[] exportarEndereco(List<Endereco> enderecoList) {
        if (enderecoList.isEmpty())
            throw new PoiException("Não é possível gerar relatorio sem endereços!");
        String[] colunas = {"Código", "CEP", "Endereço", "Número", "Bairro", "Cidade", "Estado", "Telefone", "Código Monitorador", "Nome Monitorador", "Principal"};
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Endereço");
            configExportExcel(workbook, sheet, colunas);

            int linha = 1;

            for (Endereco e : enderecoList) {
                Row row = sheet.createRow(linha++);
                row.createCell(0).setCellValue(e.getId());
                row.createCell(1).setCellValue(e.getCep());
                row.createCell(2).setCellValue(e.getEndereco());
                row.createCell(3).setCellValue(e.getNumero());
                row.createCell(4).setCellValue(e.getBairro());
                row.createCell(5).setCellValue(e.getCidade());
                row.createCell(6).setCellValue(e.getEstado());
                row.createCell(7).setCellValue(e.getTelefone());
                row.createCell(8).setCellValue(e.getMonitorador().getId());
                row.createCell(9).setCellValue(e.getMonitorador().getNomeOrRazao());
                row.createCell(10).setCellValue(e.getPrincipal() ? "Sim" : "Não");
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new PoiException("Ocorreu um erro ao gerar o relatorio de endereços!");
        }
    }

    private void configExportExcel(Workbook workbook, Sheet sheet, String[] colunas) {
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
    }

    public List<Monitorador> importar(MultipartFile file, List<IVMonitorador> validacoes) {
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx"))
            throw new PoiException("Esse tipo de arquivo não é suportado, apenas .xlsx!");

        List<Monitorador> monitoradores = new ArrayList<>();
        int linha = 0, coluna = 0;
        try (XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream())) {
            Iterator<Row> rowIterator = wb.getSheetAt(0).rowIterator();

            if (!rowIterator.hasNext()) {
                throw new PoiException("O documento está vazio!");
            } else {
                rowIterator.next();
                linha++;
                if (!rowIterator.hasNext())
                    throw new PoiException("O documento está vazio!");
            }
            while (rowIterator.hasNext()) {
                linha++;
                Monitorador m = new Monitorador();
                Row row = rowIterator.next();
                for (Cell cell : row) {
                    int index = cell.getColumnIndex();
                    coluna = index + 1;

                    switch (index) {
                        case 0 -> m.setTipo(TipoPessoa.valueOf(obterValor(cell).toUpperCase()));
                        case 1 -> m.setCnpj(obterValor(cell));
                        case 2 -> m.setRazao(obterValor(cell));
                        case 3 -> m.setInscricao(obterValor(cell));
                        case 4 -> m.setCpf(obterValor(cell));
                        case 5 -> m.setNome(obterValor(cell));
                        case 6 -> m.setRg(obterValor(cell));
                        case 7 -> m.setData(converteData(cell.getDateCellValue()));
                        case 8 -> m.setEmail(obterValor(cell));
                        case 9 -> m.setAtivo(obterValor(cell).equalsIgnoreCase("Ativo"));
                    }
                }
                validacoes.forEach(v -> v.validar(m));
                monitoradores.add(m);
                coluna = 0;
                verificaDuplicados(monitoradores);
            }
        } catch (ValidacaoException e) {
            throw new PoiException(e.getMessage() + " Linha: " + linha);
        } catch (PoiException ex) {
            throw new PoiException(ex.getMessage());
        } catch (Exception ex) {
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            throw new PoiException("Erro na Linha: " + linha + " Coluna: " + coluna);
        }
        return monitoradores;
    }

    private static LocalDate converteData(Date data) {
        if (data == null)
            throw new PoiException("O campo Data é obrigatório!");
        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static void verificaDuplicados(List<Monitorador> monitoradores) {
        for (int i = monitoradores.size() - 1; i >= 1; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (monitoradores.get(i).getCpf() != null && Objects.equals(monitoradores.get(i).getCpf(), monitoradores.get(j).getCpf()))
                    throw new PoiException("Esse CPF já foi digitado!");
                if (monitoradores.get(i).getCnpj() != null && Objects.equals(monitoradores.get(i).getCnpj(), monitoradores.get(j).getCnpj()))
                    throw new PoiException("Esse CNPJ já foi digitado!");
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
