package dev.erikmota.desafiounikamain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String endereco;
    @NotBlank
    private String numero;
    @NotBlank
    private String cep;
    @NotBlank
    private String bairro;
    @NotBlank
    private String telefone;
    @NotBlank
    private String cidade;
    @NotBlank
    private String estado;
    @NotBlank
    private String principal;
    @ManyToOne(optional = false)
    @JoinColumn(name = "monitorador")
    private Monitorador monitorador;

    public Endereco(){

    }

    public Endereco(String endereco, String numero, String cep, String bairro, String telefone, String cidade, String estado, String principal, Monitorador monitorador) {
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
