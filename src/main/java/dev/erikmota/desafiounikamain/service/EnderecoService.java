package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.validacoes.IValidacaoEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EnderecoService {
    @Autowired
    private EnderecoRepository repository;
    @Autowired
    private MonitoradorRepository monitoradorRepository;
    @Autowired
    private List<IValidacaoEndereco> validacoes;
    private final ViaCepService viaCepService = new ViaCepService();
    private final JasperService jasperService = new JasperService();

    public void cadastrar(Endereco e, Long idMonitorador){
        e.setMonitorador(monitoradorRepository.getReferenceById(idMonitorador));
        validacoes.forEach(v -> v.validar(e));
        repository.save(e);
    }

    public void editar(Long idE, Long idM, Endereco e){

        Monitorador m = monitoradorRepository.getReferenceById(idM);
        Endereco novoEndereco = repository.getReferenceById(idE);
        e.setMonitorador(m);
        novoEndereco.editar(e);
    }

    public List<?> listar(){
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao listar os endereços");
        }
    }

    public void excluir(Long id){
        Endereco e = repository.getReferenceById(id);
        String cep = e.getCep().replaceAll("[^0-9]", "");
        if (repository.existsByCep(cep))
            repository.delete(e);
        else
            throw new ValidacaoException("Este cep não está cadastrado");
    }

    public Endereco buscarCep(String cep){
        cep = cep.replaceAll("[^0-9]", "");
        if (cep.length() == 8)
            return viaCepService.buscarCep(cep);
        else
            throw new ValidacaoException("Esse cep é inválido");
    }

    public List<Endereco> filtrar(String text, String estado, String cidade, Long monitorador) {
        return repository.filtrar(text, estado, cidade, monitorador);
    }

    public byte[] gerarRelatorioAll(){
        List<Endereco> enderecos = repository.findAll();
        Collections.sort(enderecos);
        return jasperService.gerarPdfEndereco(enderecos);
    }

    public byte[] gerarRelatorio(Long id){
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(repository.getReferenceById(id));
        return jasperService.gerarPdfEndereco(enderecos);
    }
}
