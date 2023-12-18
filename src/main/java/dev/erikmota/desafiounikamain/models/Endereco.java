package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    private String numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private String estado;
    private String principal;
    @ManyToOne(optional = false)
    @JoinColumn(name = "monitorador_id")
    @JsonBackReference
    private Monitorador monitorador;

    public Endereco() {

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

    public Long getId() {
        return id;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Monitorador getMonitorador() {
        return monitorador;
    }

    public void setMonitorador(Monitorador monitorador) {
        this.monitorador = monitorador;
    }

    public Long getMonitorador_id(){
        return monitorador.getId();
    }
}
