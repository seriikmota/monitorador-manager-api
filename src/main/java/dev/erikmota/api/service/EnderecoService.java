package dev.erikmota.api.service;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.repository.EnderecoRepository;
import dev.erikmota.api.repository.MonitoradorRepository;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import dev.erikmota.api.service.validacoes.IVEndereco;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final MonitoradorRepository monitoradorRepository;
    private final List<IVEndereco> validacoes;
    private final ViaCepService viaCepService;
    private final JasperService jasperService;
    private final PoiService poiService;

    public EnderecoService(EnderecoRepository enderecoRepository, MonitoradorRepository monitoradorRepository, List<IVEndereco> validacoes, ViaCepService viaCepService, JasperService jasperService, PoiService poiService) {
        this.enderecoRepository = enderecoRepository;
        this.monitoradorRepository = monitoradorRepository;
        this.validacoes = validacoes;
        this.viaCepService = viaCepService;
        this.jasperService = jasperService;
        this.poiService = poiService;
    }

    public void cadastrar(Endereco e, Long monitoradorId){
        e.setMonitorador(monitoradorRepository.getReferenceById(monitoradorId));
        validacoes.forEach(v -> v.validar(e));
        enderecoRepository.save(e);
    }

    public void editar(Endereco e, Long enderecoId, Long monitoradorId){
        if (enderecoRepository.existsById(enderecoId) && monitoradorRepository.existsById(monitoradorId)){
            Endereco endereco = enderecoRepository.getReferenceById(enderecoId);
            e.setMonitorador(monitoradorRepository.getReferenceById(monitoradorId));
            validacoes.forEach(v -> v.validar(e));
            endereco.editar(e);
        }
        else if (!enderecoRepository.existsById(enderecoId))
            throw new ValidacaoException("Esse endereço não foi encontrado!");
        else
            throw new ValidacaoException("Esse monitorador não foi encontrado!");
    }

    public void excluir(Long id) {
        if (enderecoRepository.existsById(id))
            enderecoRepository.delete(enderecoRepository.getReferenceById(id));
        else
            throw new ValidacaoException("Esse endereço não foi encontrado!");
    }

    public List<Endereco> listar(){
        List<Endereco> enderecos = enderecoRepository.findAll();
        Collections.sort(enderecos);
        return enderecos;
    }

    public List<Endereco> filtrar(String text, String estado, String cidade, Long monitorador) {
        List<Endereco> enderecos = enderecoRepository.filtrar(text, estado, cidade, monitorador);
        Collections.sort(enderecos);
        return enderecos;
    }

    public byte[] gerarRelatorioPdf(Long id, String text, String estado, String cidade, Long monitorador){
        return jasperService.gerarPdfEndereco(id, text, cidade, estado, monitorador);
    }

    public byte[] gerarRelatorioExcel(Long id, String text, String estado, String cidade, Long monitorador){
        List<Endereco> enderecos;
        if (id == null)
            enderecos = enderecoRepository.filtrar(text, estado, cidade, monitorador);
        else
            enderecos = List.of(enderecoRepository.getReferenceById(id));
        enderecos.forEach(e -> e.setMonitorador(monitoradorRepository.getReferenceById(monitorador)));
        return poiService.exportarEndereco(enderecos);
    }

    public Endereco buscarCep(String cep){
        return viaCepService.buscarCep(cep);
    }
}
