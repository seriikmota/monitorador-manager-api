package dev.erikmota.desafiounikamain.controllers;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.MonitoradorService;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/monitorador")
public class MonitoradorController {

    @Autowired
    private MonitoradorService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Monitorador m, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                service.cadastrar(m);
                return ResponseEntity.ok().body("Success: Cadastro realizado com sucesso!");
            }
            else {
                StringBuilder errorMessage = new StringBuilder("Erro:");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessage.append(" ").append(error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errorMessage.toString());
            }
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody @Valid Monitorador m, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                service.editar(id, m);
                return ResponseEntity.ok().body("Success: Cadastro modificado com sucesso!");
            }
            else {
                String errorMessage = bindingResult.getFieldError().getDefaultMessage();
                return ResponseEntity.badRequest().body(errorMessage);
            }
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.ok().body("Success: Cadastro excluido com sucesso!");
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Monitorador>> listar() {
        List<Monitorador> monitoradores = service.listar();
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Monitorador>> filtrarNome(@PathVariable String nome) {
        List<Monitorador> monitoradores = service.filtrarNome(nome);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<List<Monitorador>> filtrarCpf(@PathVariable String cpf) {
        List<Monitorador> monitoradores = service.filtrarCpf(cpf);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<List<Monitorador>> filtrarCnpj(@PathVariable String cnpj) {
        List<Monitorador> monitoradores = service.filtrarCnpj(cnpj);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/ativo/{ativo}")
    public ResponseEntity<List<Monitorador>> filtrarAtivo(@PathVariable Boolean ativo) {
        List<Monitorador> monitoradores = service.filtrarAtivo(ativo);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/tipoPessoa/{tipoPessoa}")
    public ResponseEntity<List<Monitorador>> filtrarTipoPessoa(@PathVariable TipoPessoa tipoPessoa) {
        List<Monitorador> monitoradores = service.filtrarTipoPessoa(tipoPessoa);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Monitorador>> filtrar(@RequestParam(name = "text", required = false) String text,
                                                     @RequestParam(name = "ativo", required = false) Boolean ativo,
                                                     @RequestParam(name = "tipoPessoa", required = false) TipoPessoa tipoPessoa){
        List<Monitorador> monitoradores = service.filtrar(text, ativo, tipoPessoa);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/modelo")
    public ResponseEntity<PathResource> modelo() {
        Path path = service.gerarModelo();
        PathResource resource = new PathResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                .body(resource);
    }

    @PostMapping("/importar")
    @Transactional
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        try {
            service.importar(file);
            return ResponseEntity.ok().body("Success: Importação realizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
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
