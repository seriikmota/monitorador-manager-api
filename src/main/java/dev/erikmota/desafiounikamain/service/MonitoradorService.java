package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoradorService {

    @Autowired
    private MonitoradorRepository repository;

    public void cadastrar(Monitorador m){
        if (!repository.existsByCpfOrCnpj(m.getCpf(), m.getCnpj()))
            repository.save(m);
        else
            throw new ValidacaoException("CPF/CNPJ já existe!");
    }

    public void editar(Monitorador m){
        Monitorador novoMonitorador = repository.getReferenceById(m.getId());
        novoMonitorador.editar(m);
    }

    public List<Monitorador> listar(){
        return repository.findAll();
    }

    public void excluir(Monitorador m){
        if (repository.existsByCpfOrCnpj(m.getCpf(), m.getCnpj()))
            repository.delete(m);
        else
            throw new ValidacaoException("Esse monitorador não está cadastrado!");
    }
}
