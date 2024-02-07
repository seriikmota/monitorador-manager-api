package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.validacoes.IVMonitorador;
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
        String[] colunas = {"Tipo Pessoa", "CNPJ", "Razao Social", "Inscrição Estadual", "CPF", "Nome", "RG", "Data", "Email", "Ativo"};
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monitorador");
            configExportExcel(workbook, sheet, colunas);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ValidacaoException("Erro ao gerar o modelo para importação de monitoradores!");
        }
    }

    public byte[] exportarMonitorador(List<Monitorador> monitoradorList) {
        if (monitoradorList.isEmpty())
            throw new ValidacaoException("Não é possível gerar relatorio sem monitoradores!");
        String[] colunas = {"Código", "Tipo Pessoa", "CNPJ", "Razao Social", "Inscrição Estadual", "CPF", "Nome", "RG", "Data", "Email", "Ativo"};
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monitorador");
            configExportExcel(workbook, sheet, colunas);

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
                row.createCell(8).setCellValue(m.getData());
                row.createCell(9).setCellValue(m.getEmail());
                row.createCell(10).setCellValue(m.getAtivo() ? "Sim" : "Não");
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ValidacaoException("Erro ao gerar o relatorio de monitoradores!");
        }
    }

    public byte[] exportarEndereco(List<Endereco> enderecoList) {
        if (enderecoList.isEmpty())
            throw new ValidacaoException("Não é possível gerar relatorio sem endereços!");
        String[] colunas = {"Código", "CEP", "Endereço", "Número", "Bairro", "Cidade", "Estado", "Telefone", "Monitorador", "Principal"};
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
                row.createCell(8).setCellValue(e.getMonitorador().getNomeOrRazao());
                row.createCell(9).setCellValue(e.getPrincipal() ? "Sim" : "Não");
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ValidacaoException("Erro ao gerar o relatorio de endereços!");
        }
    }

    private void configExportExcel(Workbook workbook, Sheet sheet, String[] colunas){
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
            throw new ValidacaoException("Esse tipo de arquivo não é suportado, apenas .xlsx!");

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
            throw new ValidacaoException("Erro na Linha: " + linha + " Coluna: " + coluna);
        }
        return monitoradores;
    }

    private static LocalDate converteData(Date data) {
        if (data == null)
            throw new ValidacaoException("O campo Data é obrigatório!");

        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static void verificaDuplicados(List<Monitorador> monitoradores) {
        for (int i = monitoradores.size() - 1; i >= 1; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (monitoradores.get(i).getCpf() != null && Objects.equals(monitoradores.get(i).getCpf(), monitoradores.get(j).getCpf()))
                    throw new ValidacaoException("Esse CPF já foi digitado!");
                if (monitoradores.get(i).getCnpj() != null && Objects.equals(monitoradores.get(i).getCnpj(), monitoradores.get(j).getCnpj()))
                    throw new ValidacaoException("Esse CNPJ já foi digitado!");
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
