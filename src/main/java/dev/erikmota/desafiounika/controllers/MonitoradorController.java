package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.MonitoradorService;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import dev.erikmota.desafiounika.service.exceptions.JasperException;
import dev.erikmota.desafiounika.service.exceptions.PoiException;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/monitorador")
@CrossOrigin(origins = {"http://localhost:8080/", "http://localhost:4200/"})
public class MonitoradorController {

    private final MonitoradorService service;

    public MonitoradorController(MonitoradorService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Monitorador m, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Erro: " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            service.cadastrar(m);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody @Valid Monitorador m, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Erro: " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            service.editar(m, id);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @GetMapping
    public ResponseEntity<List<Monitorador>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(@RequestParam(name = "text", required = false) String text,
                                     @RequestParam(name = "ativo", required = false) Boolean ativo,
                                     @RequestParam(name = "tipo", required = false) TipoPessoa tipoPessoa) {
        try {
            return ResponseEntity.ok(service.filtrar(text, ativo, tipoPessoa));
        } catch (Exception ex){
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a filtragem dos monitoradores!");
        }
    }

    @GetMapping("/pdf")
    public ResponseEntity<?> relatorioPdf(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "text", required = false) String text,
                                          @RequestParam(name = "ativo", required = false) Boolean ativo,
                                          @RequestParam(name = "tipo", required = false) TipoPessoa tipo) {
        try {
            byte[] relatorioBytes = service.gerarRelatorioPdf(id, text, ativo, tipo);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
            String fileName = "RelatorioM" + date.format(formatter) + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(relatorioBytes));
        } catch (JasperException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @GetMapping("/excel")
    public ResponseEntity<?> relatorioExcel(@RequestParam(name = "id", required = false) Long id,
                                            @RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "ativo", required = false) Boolean ativo,
                                            @RequestParam(name = "tipo", required = false) TipoPessoa tipo) {
        try {
            byte[] relatorioBytes = service.gerarRelatorioExcel(id, text, ativo, tipo);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
            String fileName = "RelatorioM" + date.format(formatter) + ".xlsx";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(relatorioBytes));
        } catch (ValidacaoException | PoiException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @PostMapping("/importar")
    @Transactional
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        try {
            service.importar(file);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException | PoiException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/importar/modelo")
    public ResponseEntity<?> modelo() {
        try {
            byte[] modeloBytes = service.gerarModelo();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ModeloMonitorador.xlsx\"")
                    .body(new ByteArrayResource(modeloBytes));
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.out.println(ex.getClass() + ": " + ex.getMessage());
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }
}


