package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.validacoes.IValidacaoMonitorador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoradorService {

    @Autowired
    private MonitoradorRepository repository;

    @Autowired
    private List<IValidacaoMonitorador> validacoes;

    public void cadastrar(Monitorador m){
        validacoes.forEach(v -> v.validar(m));
        repository.save(m);
    }

    public void editar(Long id, Monitorador m){

        Monitorador novoMonitorador = repository.getReferenceById(id);
        novoMonitorador.editar(m);
    }

    public List<Monitorador> listar(){
        return repository.findAll();
    }

    public void excluir(Long id){
        Monitorador m = repository.getReferenceById(id);
        if (repository.existsByCpf(m.getCpf()))
            repository.delete(m);
        else
            throw new ValidacaoException("Esse monitorador não está cadastrado!");
    }


}
