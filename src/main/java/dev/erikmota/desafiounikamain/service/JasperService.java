package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JasperService {
    private Path ultimoPath;
    public Path gerarPdf(List<?> lista) {
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
            if (ultimoPath!=null) Files.deleteIfExists(ultimoPath);
            Path currentRelativePath = Paths.get("");
            String jasperFilePath = currentRelativePath.toAbsolutePath() + file;

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, params, dataSource);
            byte[] report = JasperExportManager.exportReportToPdf(jasperPrint);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter fData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH-mm-ss");
            if (lista.size() != 1)
                return writeBytestoFileNio("src/main/resources/relatorios/RelatorioG" + date.format(fData) + "T" + date.format(fHora) + ".pdf", report);
            else
                return writeBytestoFileNio("src/main/resources/relatorios/RelatorioI" + date.format(fData) + "T" + date.format(fHora) + ".pdf", report);

        } catch (Exception e) {
            System.out.println("Erro ao gerar relatorio");
        }
        return null;
    }

    private Path writeBytestoFileNio(String fileOutput, byte[] bytes) {
        try {
            Path path = Paths.get(fileOutput);
            Files.write(path, bytes);
            ultimoPath = path;
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
