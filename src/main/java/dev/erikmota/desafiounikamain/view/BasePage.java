package dev.erikmota.desafiounikamain.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erikmota.desafiounikamain.view.modelsView.Endereco;
import dev.erikmota.desafiounikamain.view.modelsView.Monitorador;
import org.apache.wicket.markup.html.WebPage;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class BasePage extends WebPage {

    public List<Endereco> enderecoList;
    public List<Monitorador> monitoradorList;
    public ObjectMapper mapper = new ObjectMapper();
    public static ClientHttpConfiguration client = new ClientHttpConfiguration();

    public <T> List<T> atualizarLista(String endereco, Class<T> classe) {
        List<T> lista = new ArrayList<>();
        try {
            HttpResponse<String> response = client.requestGet(endereco);
            String json = response.body();
            lista = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, classe));

        } catch (Exception e) {
            System.out.println("Erro ao atualizar lista");
        }

        return lista;
    }

    public void MapEndereco(){
        enderecoList.forEach(endereco ->
                monitoradorList.stream()
                        .filter(monitorador -> monitorador.getEnderecos().stream()
                                .anyMatch(enderecoMonitorador -> enderecoMonitorador.getCep().equals(endereco.getCep())))
                        .findFirst()
                        .ifPresent(endereco::setMonitorador)
        );
    }
}
