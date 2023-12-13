package dev.erikmota.desafiounikamain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

public class Endereco {
    private Long id;
    private String endereco;
    private int numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private String estado;
    private String principal;
    private Monitorador monitorador;

    public Endereco(){

    }

    public Endereco(String endereco, int numero, String cep, String bairro, String telefone, String cidade, String estado, String principal, Monitorador monitorador) {
        this.endereco = endereco;
        this.numero = numero;
        this.cep = cep;
        this.bairro = bairro;
        this.telefone = telefone;
        this.cidade = cidade;
        this.estado = estado;
        this.principal = principal;
        this.monitorador = monitorador;
    }

    public void editar(Endereco e) {
        this.endereco = e.endereco;
        this.numero = e.numero;
        this.cep = e.cep;
        this.bairro = e.bairro;
        this.telefone = e.telefone;
        this.cidade = e.cidade;
        this.estado = e.estado;
        this.principal = e.principal;
        this.monitorador = e.monitorador;
    }
}
