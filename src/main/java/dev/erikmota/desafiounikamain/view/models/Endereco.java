package dev.erikmota.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Endereco implements Serializable {
    private Long id;
    private String endereco;
    private String numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private String estado;
    private String principal;
    @JsonProperty("monitorador_id")
    private Long monitorador;
    //private Long monitoradorId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    /*public Long getMonitoradorId() {
        return monitoradorId;
    }

    public void setMonitoradorId(Long monitoradorId) {
        this.monitoradorId = monitoradorId;
    }*/

    public Long getMonitorador() {
        return monitorador;
    }

    public void setMonitorador(Long monitorador) {
        this.monitorador = monitorador;
    }

    public String toString() {
        return "Endereco{" +
                "id=" + id +
                ", endereco='" + endereco + '\'' +
                ", numero='" + numero + '\'' +
                ", cep='" + cep + '\'' +
                ", bairro='" + bairro + '\'' +
                ", telefone='" + telefone + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", principal='" + principal + '\'' +
                ", monitorador=" + monitorador +
                '}';
    }
}
