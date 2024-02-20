package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.MonitoradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.RouterOperation;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/monitorador")
@CrossOrigin(origins = {"http://localhost:8080/", "http://localhost:4200/"})
@Tag(name = "Monitorador")
public class MonitoradorController {
    private final MonitoradorService service;

    public MonitoradorController(MonitoradorService service) {
        this.service = service;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Monitorador m) {
        if (m.isNull()) return ResponseEntity.badRequest().body("Requisição Vazia!");
        service.cadastrar(m);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody @Valid Monitorador m) {
        if (m.isNull()) return ResponseEntity.badRequest().body("Requisição Vazia!");
        service.editar(m, id);
        return ResponseEntity.ok().build();
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok().build();
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Monitorador.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<List<Monitorador>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Monitorador.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(@RequestParam(name = "text", required = false) String text,
                                     @RequestParam(name = "ativo", required = false) Boolean ativo,
                                     @RequestParam(name = "tipo", required = false) TipoPessoa tipoPessoa) {
        return ResponseEntity.ok(service.filtrar(text, ativo, tipoPessoa));
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/pdf", schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
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

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
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

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/importar")
    @Transactional
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        service.importar(file);
        return ResponseEntity.ok().build();
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/importar/modelo")
    public ResponseEntity<?> modelo() {
        byte[] modeloBytes = service.gerarModelo();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"ModeloMonitorador.xlsx\"")
                .body(new ByteArrayResource(modeloBytes));
    }
}


