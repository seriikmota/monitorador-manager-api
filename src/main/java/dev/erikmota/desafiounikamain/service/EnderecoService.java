package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.validacoes.IValidacaoEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void cadastrar(Endereco e){
        validacoes.forEach(v -> v.validar(e));

        repository.save(e);
    }

    public void editar(Long idM, Long idE, Endereco e){

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
        if (repository.existsByCep(e.getCep()))
            repository.delete(e);
        else
            throw new ValidacaoException("Este cep não está cadastrado");
    }

    public List<Endereco> listarPorMonitorador(Long id) {
        return repository.findByMonitoradorId(id);
    }

    public String buscarCep(String cep){
        cep = cep.replaceAll("[^0-9]", "");
        if (cep.length() == 8)
            return viaCepService.buscarCep(cep);
        else
            throw new ValidacaoException("Esse cep é inválido");
    }
}
