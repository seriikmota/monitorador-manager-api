package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    public void cadastrar(Endereco e){
        if (!repository.existsByCep(e.getCep()))
            repository.save(e);
        else
            throw new ValidacaoException("Este cep já está cadastrado");
    }

    public void editar(Endereco e){
        Endereco novoEndereco = repository.getReferenceById(e.getId());
        novoEndereco.editar(e);
    }

    public List<Endereco> listar(){
        return repository.findAll();
    }

    public void excluir(Endereco e){
        if (repository.existsByCep(e.getCep()))
            repository.delete(e);
        else
            throw new ValidacaoException("Este cep não está cadastrado");
    }

}
