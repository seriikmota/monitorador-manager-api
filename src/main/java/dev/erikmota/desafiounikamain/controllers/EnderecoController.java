package dev.erikmota.desafiounikamain.controllers;

import dev.erikmota.desafiounikamain.models.Endereco;
import dev.erikmota.desafiounikamain.service.EnderecoService;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @PostMapping()
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestParam Long idMonitorador, @RequestBody @Valid Endereco e, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                service.cadastrar(e, idMonitorador);
                return ResponseEntity.ok().body("Success: Cadastro realizado com sucesso!");
            }
            else {
                StringBuilder errorMessage = new StringBuilder("Erro:");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessage.append(" ").append(error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errorMessage.toString());
            }
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body("Erro: " + ex.getMessage());
        }
    }

    @PutMapping("/{idEndereco}")
    @Transactional
    public ResponseEntity<?> editar(@PathVariable Long idEndereco,
                                    @RequestParam Long idMonitorador,
                                    @RequestBody @Valid Endereco e, BindingResult bindingResult){
        try {
            if (!bindingResult.hasErrors()) {
                service.editar(idEndereco, idMonitorador, e);
                return ResponseEntity.ok().body("Success: Cadastro modificado com sucesso!");
            }
            else {
                String errorMessage = bindingResult.getFieldError().getDefaultMessage();
                return ResponseEntity.badRequest().body(errorMessage);

            }
        } catch (ValidacaoException | EntityNotFoundException ex){
            return ResponseEntity.badRequest().body("Erro: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> excluir(@PathVariable Long id){
        try {
            service.excluir(id);
            return ResponseEntity.ok().body("Success: Cadastro excluido com sucesso!");
        } catch (ValidacaoException | EntityNotFoundException ex){
            return ResponseEntity.badRequest().body("Erro: " + ex.getMessage());
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
    public ResponseEntity<?> relatorio(@RequestParam(name = "id", required = false) Long id) {
        try {
            String fileName = "RelatorioE" + (id != null ? "Individual" : "Geral");

            byte[] relatorioBytes = (id != null) ? service.gerarRelatorio(id) : service.gerarRelatorioAll();

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
            fileName += date.format(formatter) + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(relatorioBytes));

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
}
