package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.EnderecoViaCep;
import dev.erikmota.desafiounikamain.models.Monitorador;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class JasperService {
    public Path gerarPdfMonitorador(List<?> lista) {
        String file;
        if (lista.size() != 1){
            file = "/src/main/resources/reports/RelatorioMGeral.jasper";
            return gerarPdf(file, lista, "RelatorioMG");
        }
        else{
            file = "/src/main/resources/reports/RelatorioMIndividual.jasper";
            return gerarPdf(file, lista, "RelatorioMI");
        }
    }

    public Path gerarPdfEndereco(List<?> lista) {
        String file;
        if (lista.size() != 1){
            file = "/src/main/resources/reports/RelatorioEGeral.jasper";
            return gerarPdf(file, lista, "RelatorioEG");
        }
        else{
            file = "/src/main/resources/reports/RelatorioEIndividual.jasper";
            return gerarPdf(file, lista, "RelatorioEI");
        }
    }

    private Path gerarPdf(String file, List<?> lista, String nomeRelatorio){
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
        try {
            excluirRelatorios();
            Path currentRelativePath = Paths.get("");
            String jasperFilePath = currentRelativePath.toAbsolutePath() + file;

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, new HashMap<>(), dataSource);
            byte[] report = JasperExportManager.exportReportToPdf(jasperPrint);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter fData = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH-mm-ss");
            if (lista.size() != 1)
                return writeBytestoFileNio("src/main/resources/relatorios/" + nomeRelatorio + date.format(fData) + "T" + date.format(fHora) + ".pdf", report);
            else
                return writeBytestoFileNio("src/main/resources/relatorios/" + nomeRelatorio + date.format(fData) + "T" + date.format(fHora) + ".pdf", report);

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Erro ao gerar relatorio endereco");
        }
        return null;
    }

    private Path writeBytestoFileNio(String fileOutput, byte[] bytes) {
        try {
            Path path = Paths.get(fileOutput);
            Files.write(path, bytes);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void excluirRelatorios(){
        File pasta = new File("src/main/resources/relatorios");
        if (pasta.isDirectory()) {
            File[] arquivos = pasta.listFiles(File::isFile);
            if (arquivos != null) {
                Arrays.stream(arquivos).forEach(File::delete);
            }
        }
    }

}
