package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.MonitoradorService;
import dev.erikmota.desafiounika.service.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/monitorador")
@CrossOrigin(origins = {"http://localhost:8080/", "http://localhost:4200/"})
public class MonitoradorController {

    @Autowired
    private MonitoradorService service;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid Monitorador m, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                service.cadastrar(m);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } else {
                StringBuilder errorMessage = new StringBuilder("Erro:");
                bindingResult.getFieldErrors().forEach(error ->
                        errorMessage.append(" ").append(error.getDefaultMessage())
                );
                return ResponseEntity.badRequest().body(errorMessage.toString());
            }
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody @Valid Monitorador m, BindingResult bindingResult) {
        try {
            if (!bindingResult.hasErrors()) {
                service.editar(id, m);
                return ResponseEntity.ok().build();
            } else {
                String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
                return ResponseEntity.badRequest().body(errorMessage);
            }
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Monitorador> monitoradores = service.listar();
            return ResponseEntity.ok(monitoradores);
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Monitorador>> filtrar(@RequestParam(name = "text", required = false) String text,
                                                     @RequestParam(name = "ativo", required = false) Boolean ativo,
                                                     @RequestParam(name = "tipo", required = false) TipoPessoa tipoPessoa
    ) {
        List<Monitorador> monitoradores = service.filtrar(text, ativo, tipoPessoa);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/relatorioPdf")
    public ResponseEntity<?> relatorioPdf(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "text", required = false) String text,
                                          @RequestParam(name = "ativo", required = false) Boolean ativo,
                                          @RequestParam(name = "tipo", required = false) TipoPessoa tipo) {
        try {

            byte[] relatorioBytes = service.gerarRelatorioPdf(id, text, ativo, tipo);

            String fileName = "RelatorioM";
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

    @GetMapping("/relatorioExcel")
    public ResponseEntity<?> relatorioExcel(@RequestParam(name = "id", required = false) Long id,
                                            @RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "ativo", required = false) Boolean ativo,
                                            @RequestParam(name = "tipo", required = false) TipoPessoa tipo) {
        try {
            byte[] relatorioBytes = service.gerarRelatorioExcel(id, text, ativo, tipo);

            String fileName = "RelatorioM";
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
            fileName += date.format(formatter) + ".xlsx";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(relatorioBytes));

        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/importar")
    @Transactional
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        try {
            service.importar(file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getCause());
        }
    }

}


