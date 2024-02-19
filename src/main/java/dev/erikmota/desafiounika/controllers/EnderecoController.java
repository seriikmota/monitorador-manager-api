package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/endereco")
@CrossOrigin(origins = {"http://localhost:8080/", "http://localhost:4200/"})
public class EnderecoController {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestParam(name = "monitoradorId") Long monitoradorId, @RequestBody @Valid Endereco e) {
        if (e.isNull()) return ResponseEntity.badRequest().body("Resquisição Vazia!");
        service.cadastrar(e, monitoradorId);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{enderecoId}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long enderecoId, @RequestParam(name = "monitoradorId") Long monitoradorId, @RequestBody @Valid Endereco e) {
        if (e.isNull()) return ResponseEntity.badRequest().body("Resquisição Vazia!");
        service.editar(e, enderecoId, monitoradorId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(@RequestParam(name = "text", required = false) String text,
                                     @RequestParam(name = "cidade", required = false) String cidade,
                                     @RequestParam(name = "estado", required = false) String estado,
                                     @RequestParam(name = "monitoradorId", required = false) Long monitoradorId) {
        return ResponseEntity.ok(service.filtrar(text, estado, cidade, monitoradorId));
    }

    @GetMapping("/pdf")
    public ResponseEntity<?> relatorioPdf(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "text", required = false) String text,
                                          @RequestParam(name = "cidade", required = false) String cidade,
                                          @RequestParam(name = "estado", required = false) String estado,
                                          @RequestParam(name = "monitoradorId", required = false) Long monitoradorId) {
        byte[] relatorioBytes = service.gerarRelatorioPdf(id, text, estado, cidade, monitoradorId);

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
        String fileName = "RelatorioE" + date.format(formatter) + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(relatorioBytes));
    }

    @GetMapping("/excel")
    public ResponseEntity<?> relatorioExcel(@RequestParam(name = "id", required = false) Long id,
                                            @RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "estado", required = false) String estado,
                                            @RequestParam(name = "cidade", required = false) String cidade,
                                            @RequestParam(name = "monitoradorId", required = false) Long monitoradorId) {
        byte[] relatorioBytes = service.gerarRelatorioExcel(id, text, estado, cidade, monitoradorId);

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
        String fileName = "RelatorioE" + date.format(formatter) + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new ByteArrayResource(relatorioBytes));
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<?> buscarCep(@PathVariable String cep) {
        return ResponseEntity.ok(service.buscarCep(cep));
    }
}
