package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.EnderecoService;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import dev.erikmota.desafiounika.service.exceptions.JasperException;
import dev.erikmota.desafiounika.service.exceptions.PoiException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/endereco")
@CrossOrigin(origins = {"http://localhost:8080/", "http://localhost:4200/"})
public class EnderecoController {

    @Autowired
    private EnderecoService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestParam(name = "monitoradorId") Long monitoradorId, @RequestBody @Valid Endereco e, BindingResult bindingResult) {
       if (bindingResult.hasErrors()){
           String errorMessage = "Erro: " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
           return ResponseEntity.badRequest().body(errorMessage);
       }
        try {
            service.cadastrar(e, monitoradorId);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @PutMapping("/{enderecoId}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long enderecoId, @RequestParam(name = "monitoradorId") Long monitoradorId, @RequestBody @Valid Endereco e, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String errorMessage = "Erro: " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        try {
            service.editar(e, enderecoId, monitoradorId);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id){
        try {
            service.excluir(id);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(@RequestParam(name = "text", required = false) String text,
                                                  @RequestParam(name = "cidade", required = false) String cidade,
                                                  @RequestParam(name = "estado", required = false) String estado,
                                                  @RequestParam(name = "monitoradorId", required = false) Long monitoradorId) {
        try {
            return ResponseEntity.ok(service.filtrar(text, estado, cidade, monitoradorId));
        } catch (Exception ex){
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a filtragem de endereços!");
        }
    }

    @GetMapping("/pdf")
    public ResponseEntity<?> relatorioPdf(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "text", required = false) String text,
                                          @RequestParam(name = "cidade", required = false) String cidade,
                                          @RequestParam(name = "estado", required = false) String estado,
                                          @RequestParam(name = "monitoradorId", required = false) Long monitoradorId) {
        try {
            byte[] relatorioBytes = service.gerarRelatorioPdf(id, text, estado, cidade, monitoradorId);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
            String fileName = "RelatorioE" +  date.format(formatter) + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(relatorioBytes));
        } catch (JasperException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @GetMapping("/excel")
    public ResponseEntity<?> relatorioExcel(@RequestParam(name = "id", required = false) Long id,
                                            @RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "estado", required = false) String estado,
                                            @RequestParam(name = "cidade", required = false) String cidade,
                                            @RequestParam(name = "monitoradorId", required = false) Long monitoradorId) {
        try {
            byte[] relatorioBytes = service.gerarRelatorioExcel(id, text, estado, cidade, monitoradorId);

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH-mm-ss");
            String fileName = "RelatorioE" +  date.format(formatter) + ".xlsx";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(new ByteArrayResource(relatorioBytes));
        } catch (ValidacaoException | PoiException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<?> buscarCep(@PathVariable String cep){
        try {
            return ResponseEntity.ok(service.buscarCep(cep));
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex){
            System.err.println(ex.getClass() + ": " + ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
            return ResponseEntity.badRequest().body(" Ocorreu um erro ao realizar a requisição!");
        }
    }
}
