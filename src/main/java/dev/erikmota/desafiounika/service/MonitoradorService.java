package dev.erikmota.desafiounika.service;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.repository.MonitoradorRepository;
import dev.erikmota.desafiounika.service.validacoes.IVMonitorador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MonitoradorService {
    @Autowired
    private MonitoradorRepository repository;
    @Autowired
    private List<IVMonitorador> validacoes;
    @Autowired
    private EnderecoService enderecoService;
    private final PoiService poiService = new PoiService();
    private final JasperService jasperService = new JasperService();

    public void cadastrar(Monitorador m){
        validacoes.forEach(v -> v.validar(m));
        if(m.getEnderecos().isEmpty())
            repository.save(m);
        else{
            Endereco e = m.getEnderecos().get(0);
            m.setEnderecos(Collections.emptyList());
            repository.save(m);
            enderecoService.cadastrar(e, m.getId());
        }
    }

    public void editar(Long id, Monitorador m){
        Monitorador novoMonitorador = repository.getReferenceById(id);
        m.setId(id);
        validacoes.forEach(v -> v.validar(m));
        novoMonitorador.editar(m);
    }

    public List<Monitorador> listar(){
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao listar os monitoradores");
        }
    }

    public void excluir(Long id){
        if (repository.existsById(id)){
            Monitorador m = repository.getReferenceById(id);
            repository.delete(m);
            /*if (m.getEnderecos().isEmpty())
                repository.delete(m);
            else
                throw new ValidacaoException("Não é possivel excluir um monitorador com endereço cadastrado!");*/
        }
        else
            throw new ValidacaoException("Esse monitorador não está cadastrado!");
    }

    public List<Monitorador> filtrar(String text, Boolean ativo, TipoPessoa tipoPessoa) {
        return repository.filtrar(text, ativo, tipoPessoa);
    }

    public byte[] gerarModelo(){
        return poiService.gerarModelo();
    }
    public void importar(MultipartFile file) {
        List<Monitorador> monitoradores = poiService.importar(file, validacoes);
        if (!monitoradores.isEmpty()){
            monitoradores.forEach(this::cadastrar);
        }

    }

    public byte[] gerarRelatorioPdf(Long id, String text, Boolean ativo, TipoPessoa tipo){
        List<Monitorador> monitoradores = new ArrayList<>();
        if (id == null)
            monitoradores = repository.filtrar(text, ativo, tipo);
        else
            monitoradores.add(repository.getReferenceById(id));
        Collections.sort(monitoradores);
        return jasperService.gerarPdfMonitorador(monitoradores);
    }

    public byte[] gerarRelatorioExcel(Long id, String text, Boolean ativo, TipoPessoa tipo){
        List<Monitorador> monitoradores = new ArrayList<>();
        if (id == null)
            monitoradores = repository.filtrar(text, ativo, tipo);
        else
            monitoradores.add(repository.getReferenceById(id));
        Collections.sort(monitoradores);
        return poiService.exportarMonitorador(monitoradores);
    }

}
