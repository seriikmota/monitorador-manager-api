package dev.erikmota.desafiounika;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        List<Monitorador> monitoradores = new ArrayList<>();
        int linha = 1, coluna = 0;
        try (XSSFWorkbook wb = new XSSFWorkbook(new File("ModeloMonitorador.xlsx"))) {
            Iterator<Row> rowIterator = wb.getSheetAt(0).rowIterator();

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
                monitoradores.add(m);
                coluna = 0;
                verificaDuplicados(monitoradores);
            }
        } catch (ValidacaoException e) {
            throw new ValidacaoException(e.getMessage() + " Linha: " + linha);
        } catch (Exception e) {
            throw new ValidacaoException("Linha: " + linha + " Coluna: " + coluna);
        }
    }

    private static LocalDate converteData(Date data) {
        if (data == null)
            throw new ValidacaoException("O campo Data é obrigatório!");

        return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private static String obterValor(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.format("%.0f", cell.getNumericCellValue());
            default -> null;
        };
    }

    private static void verificaDuplicados(List<Monitorador> monitoradores){
        for (int i = monitoradores.size()-1; i >= 1; i--){
            for (int j = i-1; j >= 0; j--){
                if (monitoradores.get(i).getTipo() == TipoPessoa.FISICA)
                    if (monitoradores.get(i).getCpf() != null && Objects.equals(monitoradores.get(i).getCpf(), monitoradores.get(j).getCpf()))
                        throw new ValidacaoException("Esse CPF já foi digitado!");
                else
                    if (monitoradores.get(i).getCnpj() != null && Objects.equals(monitoradores.get(i).getCnpj(), monitoradores.get(j).getCnpj()))
                        throw new ValidacaoException("Esse CNPJ já foi digitado!");
            }
        }
    }
}

