package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import jakarta.validation.constraints.Null;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JasperService {
    public void gerarPdf(List<?> lista) {
        Monitorador m = new Monitorador();
        Map<String, Object> params = new HashMap<>();
        params.put("id", m.getId());
        params.put("tipo", m.getTipo());
        params.put("cnpj", m.getCnpj());
        params.put("razao", m.getRazao());
        params.put("inscricao", m.getInscricao());
        params.put("cpf", m.getCpf());
        params.put("nome", m.getNome());
        params.put("rg", m.getRg());
        params.put("data", m.getData());
        params.put("email", m.getEmail());
        params.put("ativo", m.getAtivo());
        String file;
        if (lista.size() != 1)
            file = "/src/main/resources/reports/RelatorioGeral.jasper";
        else
            file = "/src/main/resources/reports/RelatorioIndividual.jasper";
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);

        try {
            Path currentRelativePath = Paths.get("");
            String jasperFilePath = currentRelativePath.toAbsolutePath() + file;
            System.out.println(jasperFilePath);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, params, dataSource);
            byte[] report = JasperExportManager.exportReportToPdf(jasperPrint);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter fData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH-mm-ss");
            if (lista.size() != 1)
                writeBytestoFileNio("src/main/resources/relatorios/RelatorioG" + date.format(fData) + "T" + date.format(fHora) + ".pdf", report);
            else
                writeBytestoFileNio("src/main/resources/relatorios/RelatorioI" + date.format(fData) + "T" + date.format(fHora) + ".pdf", report);

        } catch (Exception e) {
            System.out.println("Erro ao gerar relatorio");
        }
    }

    private static void writeBytestoFileNio(String fileOutput, byte[] bytes) {
        try {
            Path path = Paths.get(fileOutput);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
