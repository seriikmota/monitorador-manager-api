package dev.erikmota.desafiounikamain.controllers;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.EnderecoService;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @PostMapping("/{idMonitorador}")
    @Transactional
    public ResponseEntity<?> cadastrar(@PathVariable Long idMonitorador, @RequestBody @Valid Endereco e){
        try {
            service.cadastrar(e, idMonitorador);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{idMonitorador}/{idEndereco}")
    @Transactional
    public ResponseEntity<?> editar(@PathVariable Long idMonitorador, @PathVariable Long idEndereco,  @RequestBody @Valid Endereco e){
        try {
            service.editar(idMonitorador, idEndereco, e);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException | EntityNotFoundException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id){
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

    @GetMapping("/cep/{cep}")
    public ResponseEntity<?> buscarCep(@PathVariable String cep){
        try {
            Endereco endereco = service.buscarCep(cep);
            return ResponseEntity.ok(endereco);
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Endereco>> filtrar(@RequestParam(name = "text", required = false) String text,
                                                     @RequestParam(name = "estado", required = false) String estado,
                                                     @RequestParam(name = "cidade", required = false) String cidade,
                                                     @RequestParam(name = "monitorador", required = false) Long monitorador
                                                     ){
        List<Endereco> enderecos = service.filtrar(text, estado, cidade, monitorador);
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/relatorio")
    public ResponseEntity<PathResource> relatorio(@RequestParam(name = "id", required = false) Long id) {
        try {
            Path path;
            if (id != null)
                path = service.gerarRelatorio(id);
            else
                path = service.gerarRelatorioAll();

            PathResource resource = new PathResource(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                    .body(resource);

        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
