package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.validacoes.IVMonitorador;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MonitoradorService {
    private final MonitoradorDAO monitoradorDAO;
    private final List<IVMonitorador> validacoes;
    private final PoiService poiService;
    private final JasperService jasperService;

    public MonitoradorService(MonitoradorDAO monitoradorDAO, List<IVMonitorador> validacoes, PoiService poiService, JasperService jasperService) {
        this.monitoradorDAO = monitoradorDAO;
        this.validacoes = validacoes;
        this.poiService = poiService;
        this.jasperService = jasperService;
    }

    public void cadastrar(Monitorador m){
        validacoes.forEach(v -> v.validar(m));
        monitoradorDAO.save(m);
    }

    public void editar(Monitorador m, Long id){
        m.setId(id);
        validacoes.forEach(v -> v.validar(m));
        monitoradorDAO.edit(m);
    }

    public void excluir(Long id){
        if (monitoradorDAO.existsById(id))
            monitoradorDAO.delete(monitoradorDAO.findById(id));
    }

    public List<Monitorador> listar(){
        List<Monitorador> monitoradores = monitoradorDAO.findAll();
        Collections.sort(monitoradores);
        return monitoradores;
    }

    public List<Monitorador> filtrar(String text, Boolean ativo, TipoPessoa tipoPessoa) {
        List<Monitorador> monitoradores = monitoradorDAO.filter(text, ativo, tipoPessoa);
        Collections.sort(monitoradores);
        return monitoradores;
    }

    public byte[] gerarModelo(){
        return poiService.gerarModelo();
    }

    public void importar(MultipartFile file) {
        List<Monitorador> monitoradores = poiService.importar(file, validacoes);
        if (!monitoradores.isEmpty())
            monitoradores.forEach(this::cadastrar);
    }

    public byte[] gerarRelatorioPdf(Long id, String text, Boolean ativo, TipoPessoa tipo){
        return id != null ? jasperService.gerarPdfInd(id) : jasperService.gerarPdfGeral(text, tipo, ativo);
    }

    public byte[] gerarRelatorioExcel(Long id, String text, Boolean ativo, TipoPessoa tipo){
        if (id == null){
            List<Monitorador> monitoradores = monitoradorDAO.filter(text, ativo, tipo);
            Collections.sort(monitoradores);
            return poiService.exportarMonitorador(monitoradores);
        }
        else {
            return poiService.exportarMonitorador(List.of(monitoradorDAO.findById(id)));
        }
    }

}
