package dev.erikmota.desafiounika.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EnderecoViaCep {
    private final String cep;
    private final String logradouro;
    private final String bairro;
    private final String localidade;
    private final String uf;

    public EnderecoViaCep(String cep, String logradouro, String bairro, String localidade, String uf) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
    }

    public Endereco toEndereco(){
        return new Endereco(null, cep, logradouro, null, bairro, localidade, uf, null, null, null);
    }

}
