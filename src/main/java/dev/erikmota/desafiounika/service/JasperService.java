package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.service.exceptions.JasperException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class JasperService {
    public byte[] gerarPdfMonitorador(List<Monitorador> lista) {
        if (lista.isEmpty())
            throw new JasperException("Não é possivel gerar relatorio sem monitoradores");
        String file;
        if (lista.size() != 1)
            file = "/src/main/resources/reports/RelatorioMGeral.jasper";
        else
            file = "/src/main/resources/reports/RelatorioMIndividual.jasper";
        return gerarPdf(file, lista);
    }

    public byte[] gerarPdfEndereco(List<Endereco> lista) {
        if (lista.isEmpty())
            throw new JasperException("Não é possivel gerar relatorio sem endereços");
        String file;
        if (lista.size() != 1)
            file = "/src/main/resources/reports/RelatorioEGeral.jasper";
        else
            file = "/src/main/resources/reports/RelatorioEIndividual.jasper";
        return gerarPdf(file, lista);
    }

    private byte[] gerarPdf(String file, List<?> lista) {
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
            Path currentRelativePath = Paths.get("");
            String jasperFilePath = currentRelativePath.toAbsolutePath() + file;

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, new HashMap<>(), dataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new JasperException(" Ocorreu um erro ao gerar o relatório em PDF!");
        }
    }
}
