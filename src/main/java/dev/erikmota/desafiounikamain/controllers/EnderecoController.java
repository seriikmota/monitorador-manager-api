package dev.erikmota.desafiounikamain.controllers;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.service.EnderecoService;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
            System.out.println("Valor monitorador:");
            service.cadastrar(e);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{idMonitorador}/{idEndereco}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long idMonitorador, @PathVariable Long idEndereco,  @RequestBody @Valid Endereco e){
        try {
            service.editar(idMonitorador, idEndereco, e);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException | EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id){
        try {
            service.excluir(id);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException | EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(){
        try {
            List<?> enderecos = service.listar();
            return ResponseEntity.ok(enderecos);
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<?>> listarPorMonitorador(@PathVariable Long id){
        List<?> enderecos = service.listarPorMonitorador(id);
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<String> buscarCep(@PathVariable String cep){
        try {
            String json = service.buscarCep(cep);
            return ResponseEntity.ok(json);
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
