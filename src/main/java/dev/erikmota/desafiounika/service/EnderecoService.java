package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.dao.EnderecoDAO;
import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.repository.EnderecoRepository;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import dev.erikmota.desafiounika.service.validacoes.IVEndereco;
import org.springframework.stereotype.Service;

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

    public void cadastrar(Endereco e, Long monitoradorId){
        e.setMonitorador(monitoradorRepository.getReferenceById(monitoradorId));
        validacoes.forEach(v -> v.validar(e));
        repository.save(e);
    }

    public void editar(Endereco e, Long idE, Long monitoradorId){
        Monitorador m = monitoradorRepository.getReferenceById(monitoradorId);
        Endereco novoEndereco = repository.getReferenceById(idE);
        e.setMonitorador(m);
        validacoes.forEach(v -> v.validar(e));

        novoEndereco.editar(e);
    }

    public void excluir(Long id){
        if (repository.existsById(id))
            repository.delete(repository.getReferenceById(id));
        else
            throw new ValidacaoException("Este endereço não está cadastrado");
    }

    public List<Endereco> listar(){
        List<Endereco> enderecos = repository.findAll();
        Collections.sort(enderecos);
        return enderecos;
    }

    public List<Endereco> filtrar(String text, String estado, String cidade, Long monitorador) {
        List<Endereco> enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
        Collections.sort(enderecos);
        return enderecos;
    }

    public byte[] gerarRelatorioPdf(Long id, String text, String estado, String cidade, Long monitorador){
        if (id == null){
            List<Endereco> enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
            Collections.sort(enderecos);
            return jasperService.gerarPdfEndereco(enderecos);
        }
        else
            return jasperService.gerarPdfEndereco(List.of(repository.getReferenceById(id)));
    }

    public byte[] gerarRelatorioExcel(Long id, String text, String estado, String cidade, Long monitorador){
        if (id == null){
            List<Endereco> enderecos = enderecoDAO.filter(text, estado, cidade, monitorador);
            Collections.sort(enderecos);
            return poiService.exportarEndereco(enderecos);
        }
        else
            return poiService.exportarEndereco(List.of(repository.getReferenceById(id)));
    }

    public Endereco buscarCep(String cep){
        cep = cep.replaceAll("[^0-9]", "");
        if (cep.length() == 8)
            return viaCepService.buscarCep(cep);
        else
            throw new ValidacaoException("Esse CEP é inválido");
    }
}
