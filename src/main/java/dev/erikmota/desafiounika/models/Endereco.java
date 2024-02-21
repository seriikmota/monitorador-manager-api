package dev.erikmota.desafiounika.models;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name="endereco")
public class Endereco implements Comparable<Endereco> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String endereco;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefone;
    private Boolean principal;
    @ManyToOne(optional = false)
    @JoinColumn(name = "monitorador_id")
    @JsonIgnore
    private Monitorador monitorador;

    public Endereco() {}

    public Endereco(Long id, String cep, String endereco, String numero, String bairro, String cidade, String estado, String telefone, Boolean principal) {
        this.id = id;
        this.cep = cep;
        this.endereco = endereco;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.telefone = telefone;
        this.principal = principal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        if (!StringUtils.isBlank(cep))
            this.cep = cep.replaceAll("[^0-9]", "");
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

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (!StringUtils.isBlank(telefone))
            this.telefone = telefone.replaceAll("[^0-9]", "");
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Monitorador getMonitorador() {
        return monitorador;
    }

    public void setMonitorador(Monitorador monitorador) {
        this.monitorador = monitorador;
    }

    @Override
    public int compareTo(Endereco e) {
        int comparacaoEstado = this.estado.compareTo(e.estado);
        if (comparacaoEstado != 0) {
            return comparacaoEstado;
        }
        int comparacaoCidade = this.cidade.compareTo(e.cidade);
        if (comparacaoCidade != 0) {
            return comparacaoCidade;
        }
        return this.endereco.compareTo(e.endereco);
    }

    @Override
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
                ", principal=" + principal +
                '}';
    }

    @JsonIgnore
    @Transient
    public String getMonitorador_id() {
        if (monitorador.getTipo() == TipoPessoa.FISICA)
            return monitorador.getNome();
        else
            return monitorador.getRazao();
    }
    @JsonIgnore
    public Boolean isNull(){
        return this.cep == null
                && this.endereco == null
                && this.numero == null
                && this.bairro == null
                && this.cidade == null
                && this.estado == null
                && this.telefone == null
                && this.principal == null;
    }

}
