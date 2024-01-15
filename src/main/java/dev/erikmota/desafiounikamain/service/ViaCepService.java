package dev.erikmota.desafiounikamain.service;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ViaCepService {
    public String buscarCep (String cep) {
        String endereco = "https://viacep.com.br/ws/" + cep + "/json/";
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e){
            throw new ValidacaoException("Erro ao encontrar o cep!");
        }
    }
}
