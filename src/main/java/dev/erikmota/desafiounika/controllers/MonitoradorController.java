package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.MonitoradorService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Monitorador m) {
        if (m.isNull()) return ResponseEntity.badRequest().body("Requisição Vazia!");
        service.cadastrar(m);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody @Valid Monitorador m) {
        if (m.isNull()) return ResponseEntity.badRequest().body("Requisição Vazia!");
        service.editar(m, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Monitorador>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(@RequestParam(name = "text", required = false) String text,
                                     @RequestParam(name = "ativo", required = false) Boolean ativo,
                                     @RequestParam(name = "tipo", required = false) TipoPessoa tipoPessoa) {
        return ResponseEntity.ok(service.filtrar(text, ativo, tipoPessoa));
    }

    @GetMapping("/pdf")
    public ResponseEntity<?> relatorioPdf(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "text", required = false) String text,
                                          @RequestParam(name = "ativo", required = false) Boolean ativo,
                                          @RequestParam(name = "tipo", required = false) TipoPessoa tipo) {
        byte[] relatorioBytes = service.gerarRelatorioPdf(id, text, ativo, tipo);

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
        String fileName = "RelatorioM" + date.format(formatter) + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(relatorioBytes));
    }

    @GetMapping("/excel")
    public ResponseEntity<?> relatorioExcel(@RequestParam(name = "id", required = false) Long id,
                                            @RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "ativo", required = false) Boolean ativo,
                                            @RequestParam(name = "tipo", required = false) TipoPessoa tipo) {
        byte[] relatorioBytes = service.gerarRelatorioExcel(id, text, ativo, tipo);

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
        String fileName = "RelatorioM" + date.format(formatter) + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(relatorioBytes));
    }

    @PostMapping("/importar")
    @Transactional
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        service.importar(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/importar/modelo")
    public ResponseEntity<?> modelo() {
        byte[] modeloBytes = service.gerarModelo();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ModeloMonitorador.xlsx\"")
                .body(new ByteArrayResource(modeloBytes));
    }
}


