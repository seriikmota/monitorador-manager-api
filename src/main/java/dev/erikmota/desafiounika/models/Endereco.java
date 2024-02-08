package dev.erikmota.desafiounika.models;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

@Entity
@Table(name="endereco")
public class Endereco implements Comparable<Endereco> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String endereco;
    private String numero;
    private String cep;
    private String bairro;
    private String telefone;
    private String cidade;
    private String estado;
    private Boolean principal;
    @ManyToOne(optional = false)
    @JoinColumn(name = "monitorador_id")
    @JsonIgnore
    private Monitorador monitorador;

    public Endereco() {

    }

    public Endereco(Long id, String cep, String endereco, String numero, String bairro, String cidade, String estado, String telefone, Boolean principal, Monitorador monitorador) {
        this.id = id;
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
        if (cep != null)
            return cep.substring(0, 5) + "-" + cep.substring(5);
        else
            return null;
    }

    public void setCep(String cep) {
        if (cep != null)
            this.cep = cep.replaceAll("[^0-9]", "");
        else
            this.cep = null;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTelefone() {
        if (telefone != null)
            return String.format("(%s) %s-%s",
                    telefone.substring(0, 2),
                    telefone.substring(2, 7),
                    telefone.substring(7));
        else
            return null;
    }

    public void setTelefone(String telefone) {
        if (telefone != null)
            this.telefone = telefone.replaceAll("[^0-9]", "");
        else
            this.telefone = null;
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

}
