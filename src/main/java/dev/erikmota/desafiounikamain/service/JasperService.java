package dev.erikmota.desafiounikamain.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class JasperService {
    public byte[] gerarPdfMonitorador(List<?> lista) {
        String file;
        if (lista.size() != 1) {
            file = "/src/main/resources/reports/RelatorioMGeral.jasper";
        } else {
            file = "/src/main/resources/reports/RelatorioMIndividual.jasper";
        }
        return gerarPdf(file, lista);
    }

    public byte[] gerarPdfEndereco(List<?> lista) {
        String file;
        if (lista.size() != 1) {
            file = "/src/main/resources/reports/RelatorioEGeral.jasper";
        } else {
            file = "/src/main/resources/reports/RelatorioEIndividual.jasper";
        }
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
            throw new ValidacaoException("Erro ao gerar o relatório de endereço!");
        }
    }
}
