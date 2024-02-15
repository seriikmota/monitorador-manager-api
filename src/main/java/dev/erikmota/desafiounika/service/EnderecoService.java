package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.EnderecoDAO;
import dev.erikmota.desafiounika.dao.MonitoradorDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.validacoes.IVEndereco;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EnderecoService {
    private final EnderecoDAO enderecoDAO;
    private final MonitoradorDAO monitoradorDAO;
    private final List<IVEndereco> validacoes;
    private final ViaCepService viaCepService;
    private final JasperService jasperService;
    private final PoiService poiService;

    public EnderecoService(EnderecoDAO enderecoDAO, MonitoradorDAO monitoradorDAO, List<IVEndereco> validacoes, ViaCepService viaCepService, JasperService jasperService, PoiService poiService) {
        this.enderecoDAO = enderecoDAO;
        this.monitoradorDAO = monitoradorDAO;
        this.validacoes = validacoes;
        this.viaCepService = viaCepService;
        this.jasperService = jasperService;
        this.poiService = poiService;
    }

    public void cadastrar(Endereco e, Long monitoradorId){
        e.setMonitorador(monitoradorDAO.findById(monitoradorId));
        validacoes.forEach(v -> v.validar(e));
        enderecoDAO.save(e, monitoradorId);
    }

    public void editar(Endereco e, Long enderecoId, Long monitoradorId){
        e.setId(enderecoId);
        e.setMonitorador(monitoradorDAO.findById(monitoradorId));
        validacoes.forEach(v -> v.validar(e));
        enderecoDAO.edit(e);
    }

    public void excluir(Long id){
        if (enderecoDAO.existsById(id))
            enderecoDAO.delete(id);
    }

    public List<Endereco> listar(){
        List<Endereco> enderecos = enderecoDAO.findAll();
        Collections.sort(enderecos);
        return enderecos;
    }

    public List<Endereco> filtrar(String text, String estado, String cidade, Long monitorador) {
        List<Endereco> enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
        Collections.sort(enderecos);
        return enderecos;
    }

    public byte[] gerarRelatorioPdf(Long id, String text, String estado, String cidade, Long monitorador){
        return jasperService.gerarPdfEndereco(id, text, cidade, estado, monitorador);
    }

    public byte[] gerarRelatorioExcel(Long id, String text, String estado, String cidade, Long monitorador){
        List<Endereco> enderecos;
        if (id == null)
            enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
        else
            enderecos = List.of(enderecoDAO.findById(id));
        enderecos.forEach(e -> e.setMonitorador(monitoradorDAO.findById(monitorador)));
        return poiService.exportarEndereco(enderecos);
    }

    public Endereco buscarCep(String cep){
        return viaCepService.buscarCep(cep);
    }
}
