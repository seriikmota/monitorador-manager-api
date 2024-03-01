package dev.erikmota.api.controllers;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Endereço")
public class EnderecoController {

    private final EnderecoService service;

    public EnderecoController(EnderecoService service) {
        this.service = service;
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestParam(name = "monitoradorId") Long monitoradorId, @RequestBody @Valid Endereco e) {
        if (e.isNull()) return ResponseEntity.badRequest().body("Resquisição Vazia!");
        service.cadastrar(e, monitoradorId);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PutMapping("/{enderecoId}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long enderecoId, @RequestParam(name = "monitoradorId") Long monitoradorId, @RequestBody @Valid Endereco e) {
        if (e.isNull()) return ResponseEntity.badRequest().body("Resquisição Vazia!");
        service.editar(e, enderecoId, monitoradorId);
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
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Endereco.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping
    public ResponseEntity<List<Endereco>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Endereco.class)))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(@RequestParam(name = "text", required = false) String text,
                                     @RequestParam(name = "cidade", required = false) String cidade,
                                     @RequestParam(name = "estado", required = false) String estado,
                                     @RequestParam(name = "monitoradorId", required = false) Long monitoradorId) {
        return ResponseEntity.ok(service.filtrar(text, estado, cidade, monitoradorId));
    }

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/pdf", schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
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

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
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

    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Endereco.class))),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/cep/{cep}")
    public ResponseEntity<?> buscarCep(@PathVariable String cep) {
        return ResponseEntity.ok(service.buscarCep(cep));
    }
}
