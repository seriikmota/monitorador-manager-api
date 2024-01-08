package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
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
        if (repository.existsById(id))
            repository.delete(repository.getReferenceById(id));
        else
            throw new ValidacaoException("Esse monitorador não está cadastrado");
    }

    public List<Monitorador> filtrarNome(String nome) {
        return repository.findByNomeContains(nome);
    }

    public List<Monitorador> filtrarCpf(String cpf) {
        return repository.findByCpfContains(cpf);
    }

    public List<Monitorador> filtrarCnpj(String cnpj) {
        return repository.findByCnpjContains(cnpj);
    }

    public List<Monitorador> filtrarAtivo(Boolean ativo) {
        return repository.findByAtivo(ativo);
    }

    public List<Monitorador> filtrarTipoPessoa(TipoPessoa tipoPessoa) {
        return repository.findByTipoPessoa(tipoPessoa);
    }

    public List<Monitorador> filtrar(String text, Boolean ativo, TipoPessoa tipoPessoa) {
        return repository.filtrar(text, ativo, tipoPessoa);
    }
}
