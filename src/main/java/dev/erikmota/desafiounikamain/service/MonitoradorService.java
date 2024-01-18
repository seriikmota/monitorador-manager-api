package dev.erikmota.desafiounikamain.service;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.validacoes.IValidacaoMonitorador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MonitoradorService {
    @Autowired
    private MonitoradorRepository repository;
    @Autowired
    private List<IValidacaoMonitorador> validacoes;
    private final PoiService poiService = new PoiService();
    private final JasperService jasperService = new JasperService();

    public void cadastrar(Monitorador m){
        validacoes.forEach(v -> v.validar(m));
        if (m.getTipo() == TipoPessoa.FISICA){
            m.setCnpj(null); m.setRazao(null); m.setInscricao(null);
        } else {
            m.setCpf(null); m.setNome(null); m.setRg(null);
        }
        repository.save(m);
    }

    public void editar(Long id, Monitorador m){

        Monitorador novoMonitorador = repository.getReferenceById(id);
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
            if (m.getEnderecos().isEmpty())
                repository.delete(m);
            else
                throw new ValidacaoException("Não é possivel excluir um monitorador com endereços cadastrados!");
        }
        else
            throw new ValidacaoException("Esse monitorador não está cadastrado");
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

    public byte[] gerarRelatorioAll(){
        List<Monitorador> monitoradores = repository.findAll();
        Collections.sort(monitoradores);
        return jasperService.gerarPdfMonitorador(monitoradores);
    }

    public byte[] gerarRelatorio(Long id){
        List<Monitorador> monitoradores = new ArrayList<>();
        monitoradores.add(repository.getReferenceById(id));
        return jasperService.gerarPdfMonitorador(monitoradores);
    }

}
