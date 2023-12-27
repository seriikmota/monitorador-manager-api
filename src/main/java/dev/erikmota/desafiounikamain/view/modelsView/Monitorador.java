package dev.erikmota.desafiounikamain.view.modelsView;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class Monitorador implements Serializable {
    private Long id;
    @JsonProperty("tipoPessoa")
    private String tipoPessoa;
    private String cpf;
    private String cnpj;
    private String nome;
    @JsonProperty("razaoSocial")
    private String razaoSocial;
    private String email;
    private String rg;
    @JsonProperty("inscricaoSocial")
    private Long inscricaoSocial;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("dataNascimento")
    private String dataNascimento;
    private String ativo;
    private List<Endereco> enderecos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Long getInscricaoSocial() {
        return inscricaoSocial;
    }

    public void setInscricaoSocial(Long inscricaoSocial) {
        this.inscricaoSocial = inscricaoSocial;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public String toString() {
        return "Monitorador{" +
                "id=" + id +
                ", tipoPessoa='" + tipoPessoa + '\'' +
                ", cpf='" + cpf + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nome='" + nome + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", email='" + email + '\'' +
                ", rg='" + rg + '\'' +
                ", inscricaoSocial=" + inscricaoSocial +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", ativo='" + ativo + '\'' +
                ", \nenderecos=" + enderecos +
                '}' + "\n";
    }

}