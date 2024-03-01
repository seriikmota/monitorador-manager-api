package dev.erikmota.api.service;

import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.service.exceptions.JasperException;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class JasperService {
    private final DataSource dataSource;

    public JasperService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public byte[] gerarPdfInd(Long id) {
        String file = "/src/main/resources/reports/RelatorioMIndividual.jasper";
        HashMap<String, Object> params = new HashMap<>();
        params.put("ID", id);
        return gerarPdf(file, params);
    }

    public byte[] gerarPdfGeral(String text, TipoPessoa tipo, Boolean ativo) {
        String file = "/src/main/resources/reports/RelatorioMGeral.jasper";
        HashMap<String, Object> params = new HashMap<>();
        params.put("TEXT", text);
        params.put("TIPO", tipo);
        params.put("ATIVO", ativo);
        return gerarPdf(file, params);
    }

    public byte[] gerarPdfEndereco(Long id, String text, String cidade, String estado, Long monitorador) {
        String file = "/src/main/resources/reports/RelatorioEGeral.jasper";
        HashMap<String, Object> params = new HashMap<>();
        if (id != null)
            params.put("ID", id);
        else {
            params.put("TEXT", text);
            params.put("CIDADE", cidade);
            params.put("ESTADO", estado);
            params.put("MONITORADOR_ID", monitorador);
        }
        return gerarPdf(file, params);
    }

    private byte[] gerarPdf(String file, HashMap<String,Object> params) {
        try {
            Path currentRelativePath = Paths.get("");
            String jasperFilePath = currentRelativePath.toAbsolutePath() + file;

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFilePath, params, dataSource.getConnection());
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new JasperException("Ocorreu um erro ao gerar o relatório em PDF!");
        }
    }
}
