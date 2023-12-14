package dev.erikmota.desafiounikamain.controllers;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.service.MonitoradorService;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/monitorador")
public class MonitoradorController {

    @Autowired
    private MonitoradorService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Monitorador m){
        try {
            service.cadastrar(m);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody @Valid Monitorador m){
        try {
            service.editar(id, m);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id){
        try {
            service.excluir(id);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Monitorador>> listar(){
        List<Monitorador> monitoradores = service.listar();
        return ResponseEntity.ok(monitoradores);
    }


}
