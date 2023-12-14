package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="endereco")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String endereco;
    @NotNull
    private Long numero;
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
    @JoinColumn(name = "monitorador_id")
    private Monitorador monitorador;

    public Endereco(){

    }

    public Endereco(String endereco, Long numero, String cep, String bairro, String telefone, String cidade, String estado, String principal, Monitorador monitorador) {
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
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

    public Long getId() {
        return id;
    }
}
