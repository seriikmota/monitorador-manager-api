package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import dev.erikmota.desafiounika.service.validacoes.IVMonitorador;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MonitoradorService {
    private final MonitoradorRepository repository;
    private final MonitoradorDAO monitoradorDAO;
    private final EnderecoService enderecoService;
    private final List<IVMonitorador> validacoes;
    private final PoiService poiService;
    private final JasperService jasperService;

    public MonitoradorService(MonitoradorRepository repository, MonitoradorDAO monitoradorDAO, EnderecoService enderecoService, List<IVMonitorador> validacoes, PoiService poiService, JasperService jasperService) {
        this.repository = repository;
        this.monitoradorDAO = monitoradorDAO;
        this.enderecoService = enderecoService;
        this.validacoes = validacoes;
        this.poiService = poiService;
        this.jasperService = jasperService;
    }

    public void cadastrar(Monitorador m){
        validacoes.forEach(v -> v.validar(m));
        if (m.getEnderecos().isEmpty())
            repository.save(m);
        else {
            Endereco e = m.getEnderecos().get(0);
            m.setEnderecos(Collections.emptyList());
            repository.save(m);
            enderecoService.cadastrar(e, m.getId());
        }
    }

    public void editar(Monitorador m, Long id){
        m.setId(id);
        validacoes.forEach(v -> v.validar(m));
        Monitorador novoMonitorador = repository.getReferenceById(id);
        novoMonitorador.editar(m);
    }

    public void excluir(Long id){
        if (repository.existsById(id)){
            Monitorador m = repository.getReferenceById(id);
            repository.delete(m);
        }
        else
            throw new ValidacaoException("Esse monitorador não está cadastrado!");
    }

    public List<Monitorador> listar(){
        List<Monitorador> monitoradores = repository.findAll();
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
        if (id == null){
            List<Monitorador> monitoradores = monitoradorDAO.filter(text, ativo, tipo);
            Collections.sort(monitoradores);
            return jasperService.gerarPdfMonitorador(monitoradores);
        }
        else {
            return jasperService.gerarPdfMonitorador(List.of(repository.getReferenceById(id)));
        }
    }

    public byte[] gerarRelatorioExcel(Long id, String text, Boolean ativo, TipoPessoa tipo){
        if (id == null){
            List<Monitorador> monitoradores = monitoradorDAO.filter(text, ativo, tipo);
            Collections.sort(monitoradores);
            return poiService.exportarMonitorador(monitoradores);
        }
        else {
            return poiService.exportarMonitorador(List.of(repository.getReferenceById(id)));
        }
    }

}
