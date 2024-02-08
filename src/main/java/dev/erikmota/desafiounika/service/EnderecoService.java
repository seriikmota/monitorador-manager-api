package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.EnderecoDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.repository.EnderecoRepository;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.validacoes.IVEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EnderecoService {
    private final EnderecoRepository repository;
    private final EnderecoDAO enderecoDAO;
    private final MonitoradorRepository monitoradorRepository;
    private final List<IVEndereco> validacoes;
    private final ViaCepService viaCepService;
    private final JasperService jasperService;
    private final PoiService poiService;

    public EnderecoService(EnderecoRepository repository, EnderecoDAO enderecoDAO, MonitoradorRepository monitoradorRepository, List<IVEndereco> validacoes, ViaCepService viaCepService, JasperService jasperService, PoiService poiService) {
        this.repository = repository;
        this.enderecoDAO = enderecoDAO;
        this.monitoradorRepository = monitoradorRepository;
        this.validacoes = validacoes;
        this.viaCepService = viaCepService;
        this.jasperService = jasperService;
        this.poiService = poiService;
    }

    public void cadastrar(Endereco e, Long idM){
        e.setMonitorador(monitoradorRepository.getReferenceById(idM));
        validacoes.forEach(v -> v.validar(e));
        repository.save(e);
    }

    public void editar(Long idE, Long idM, Endereco e){
        Monitorador m = monitoradorRepository.getReferenceById(idM);
        Endereco novoEndereco = repository.getReferenceById(idE);
        e.setMonitorador(m);
        validacoes.forEach(v -> v.validar(e));

        novoEndereco.editar(e);
    }

    public List<Endereco> listar(){
        try {
            List<Endereco> enderecos = repository.findAll();
            Collections.sort(enderecos);
            return enderecos;
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao listar os endereços");
        }
    }

    public void excluir(Long id){
        if (repository.existsById(id))
            repository.delete(repository.getReferenceById(id));
        else
            throw new ValidacaoException("Este endereço não está cadastrado");
    }

    public Endereco buscarCep(String cep){
        cep = cep.replaceAll("[^0-9]", "");
        if (cep.length() == 8)
            return viaCepService.buscarCep(cep);
        else
            throw new ValidacaoException("Esse CEP é inválido");
    }

    public List<Endereco> filtrar(String text, String estado, String cidade, Long monitorador) {
        List<Endereco> enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
        Collections.sort(enderecos);
        return enderecos;
    }

    public byte[] gerarRelatorioPdf(Long id, String text, String estado, String cidade, Long monitorador){
        List<Endereco> enderecos = new ArrayList<>();
        if (id == null)
            enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
        else
            enderecos.add(repository.getReferenceById(id));
        Collections.sort(enderecos);
        return jasperService.gerarPdfEndereco(enderecos);
    }

    public byte[] gerarRelatorioExcel(Long id, String text, String estado, String cidade, Long monitorador){
        List<Endereco> enderecos = new ArrayList<>();
        if (id == null)
            enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
        else
            enderecos.add(repository.getReferenceById(id));
        Collections.sort(enderecos);
        return poiService.exportarEndereco(enderecos);
    }
}
