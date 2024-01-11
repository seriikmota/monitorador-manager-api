package dev.erikmota.desafiounikamain.controllers;

import com.mysql.cj.util.StringUtils;
import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.service.MonitoradorService;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/monitorador")
public class MonitoradorController {

    @Autowired
    private MonitoradorService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Monitorador m){
        try {
            service.cadastrar(m);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<String> editar(@PathVariable Long id, @RequestBody @Valid Monitorador m){
        try {
            service.editar(id, m);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> excluir(@PathVariable Long id){
        try {
            service.excluir(id);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Monitorador>> listar(){
        List<Monitorador> monitoradores = service.listar();
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Monitorador>> filtrarNome(@PathVariable String nome){
        List<Monitorador> monitoradores = service.filtrarNome(nome);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<List<Monitorador>> filtrarCpf(@PathVariable String cpf){
        List<Monitorador> monitoradores = service.filtrarCpf(cpf);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<List<Monitorador>> filtrarCnpj(@PathVariable String cnpj){
        List<Monitorador> monitoradores = service.filtrarCnpj(cnpj);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/ativo/{ativo}")
    public ResponseEntity<List<Monitorador>> filtrarAtivo(@PathVariable Boolean ativo){
        List<Monitorador> monitoradores = service.filtrarAtivo(ativo);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/tipoPessoa/{tipoPessoa}")
    public ResponseEntity<List<Monitorador>> filtrarTipoPessoa(@PathVariable TipoPessoa tipoPessoa){
        List<Monitorador> monitoradores = service.filtrarTipoPessoa(tipoPessoa);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<Monitorador>> filtrar(@RequestParam(name = "text", required = false) String text,
                                                     @RequestParam(name = "ativo", required = false) Boolean ativo,
                                                     @RequestParam(name = "tipoPessoa", required = false) TipoPessoa tipoPessoa
                                                     ){
        List<Monitorador> monitoradores = service.filtrar(text, ativo, tipoPessoa);
        return ResponseEntity.ok(monitoradores);
    }

    @GetMapping("/modelo")
    public ResponseEntity<String> modelo(){
        try {
            service.modeloImportar();
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/importar")
    @Transactional
    public ResponseEntity<String> importar(@RequestParam("file") MultipartFile file) {
        try {
            service.importar(file);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/relatorio")
    public ResponseEntity<String> relatorio(@RequestParam(name = "id", required = false) Long id){
        try {
            if (id != null)
                service.gerarRelatorio(id);
            else
                service.gerarRelatorioAll();
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
