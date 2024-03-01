package dev.erikmota.api.service;

import dev.erikmota.api.models.Endereco;
import dev.erikmota.api.models.Monitorador;
import dev.erikmota.api.models.TipoPessoa;
import dev.erikmota.api.repository.MonitoradorRepository;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import dev.erikmota.api.service.validacoes.IVMonitorador;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MonitoradorService {
    private final MonitoradorRepository monitoradorRepository;
    private final List<IVMonitorador> validacoes;
    private final EnderecoService enderecoService;
    private final PoiService poiService;
    private final JasperService jasperService;

    public MonitoradorService(MonitoradorRepository monitoradorRepository, List<IVMonitorador> validacoes, EnderecoService enderecoService, PoiService poiService, JasperService jasperService) {
        this.monitoradorRepository = monitoradorRepository;
        this.validacoes = validacoes;
        this.enderecoService = enderecoService;
        this.poiService = poiService;
        this.jasperService = jasperService;
    }

    public void cadastrar(Monitorador m){
        validacoes.forEach(v -> v.validar(m));
        if (m.getEnderecos().isEmpty())
            monitoradorRepository.save(m);
        else {
            Endereco e = m.getEnderecos().get(0);
            m.setEnderecos(Collections.emptyList());
            monitoradorRepository.save(m);
            enderecoService.cadastrar(e, m.getId());
        }
    }

    public void editar(Monitorador m, Long id){
        if (monitoradorRepository.existsById(id)) {
            m.setId(id);
            validacoes.forEach(v -> v.validar(m));
            Monitorador monitorador = monitoradorRepository.getReferenceById(id);
            monitorador.editar(m);
        }
        else
            throw new ValidacaoException("Esse monitorador não foi encontrado!");
    }

    public void excluir(Long id) {
        if (monitoradorRepository.existsById(id))
            monitoradorRepository.delete(monitoradorRepository.getReferenceById(id));
        else
            throw new ValidacaoException("Esse monitorador não foi encontrado!");
    }

    public List<Monitorador> listar(){
        List<Monitorador> monitoradores = monitoradorRepository.findAll();
        Collections.sort(monitoradores);
        return monitoradores;
    }

    public List<Monitorador> filtrar(String text, Boolean ativo, TipoPessoa tipoPessoa) {
        List<Monitorador> monitoradores = monitoradorRepository.filtrar(text, ativo, tipoPessoa);
        Collections.sort(monitoradores);
        return monitoradores;
    }

    public byte[] gerarModelo(){
        return poiService.gerarModelo();
    }

    public void importar(MultipartFile file) {
        List<Monitorador> monitoradores = poiService.importar(file, validacoes);
        if (!monitoradores.isEmpty())
            monitoradores.forEach(this::cadastrar);
    }

    public byte[] gerarRelatorioPdf(Long id, String text, Boolean ativo, TipoPessoa tipo){
        return id != null ? jasperService.gerarPdfInd(id) : jasperService.gerarPdfGeral(text, tipo, ativo);
    }

    public byte[] gerarRelatorioExcel(Long id, String text, Boolean ativo, TipoPessoa tipo){
        if (id == null){
            List<Monitorador> monitoradores = monitoradorRepository.filtrar(text, ativo, tipo);
            Collections.sort(monitoradores);
            return poiService.exportarMonitorador(monitoradores);
        }
        else
            return poiService.exportarMonitorador(List.of(monitoradorRepository.getReferenceById(id)));
    }

}
