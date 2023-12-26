package dev.erikmota.desafiounikamain.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erikmota.models.Endereco;
import org.apache.wicket.markup.html.WebPage;

import java.net.http.HttpResponse;
import java.util.List;

public class BasePage extends WebPage {
    public ObjectMapper mapper = new ObjectMapper();
    public static ClientHttpConfiguration client = new ClientHttpConfiguration();

    public <T> List<T> atualizarListar(String endereco, Class<?> classe) {
        List<T> lista = null;
        try {
            HttpResponse<String> response = client.requestGet(endereco);
            String json = response.body();

            lista = (List<T>) mapper.readValue(json, new TypeReference<List<Endereco>>() {});
            lista.forEach(System.out::println);


        } catch (Exception e) {
            System.out.println("Erro ao atualizar lista");
        }
        return lista;
    }
}
