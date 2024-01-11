package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class PoiService {
    public List<Monitorador> importar(MultipartFile file) {
        List<Monitorador> monitoradores = new ArrayList<>();
        try (XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
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
                            case 0: m.setTipo(TipoPessoa.valueOf(cell.getStringCellValue())); break;
                            case 1: m.setCnpj(verifyNull(cell.getStringCellValue())); break;
                            case 2: m.setRazao(verifyNull(cell.getStringCellValue())); break;
                            case 3: m.setInscricao(verifyNull(cell.getStringCellValue())); break;
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
        return monitoradores;
    }

    public void exportar(){

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
