package dev.erikmota.desafiounikamain.controllers;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.EnderecoService;
import dev.erikmota.desafiounikamain.service.MonitoradorService;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Endereco e){
        try {
            service.cadastrar(e);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/editar")
    @Transactional
    public ResponseEntity<String> editar(@RequestBody @Valid Endereco e){
        try {
            service.editar(e);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/excluir")
    @Transactional
    public ResponseEntity<String> excluir(@RequestBody @Valid Endereco e){
        try {
            service.excluir(e);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listar(){
        List<Endereco> enderecos = service.listar();
        return ResponseEntity.ok(enderecos);
    }


}
