package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="monitorador")
public class Monitorador {
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa;
    @CPF
    private String cpf;
    @CNPJ
    private String cnpj;
    private String nome;
    @Column(name = "razao_social")
    private String razaoSocial;
    private String rg;
    @Column(name = "inscricao_estadual")
    private String inscricaoEstadual;
    @NotBlank
    private String email;
    @Column(name = "data")
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate data;
    @NotNull
    private Boolean ativo;
    @OneToMany(mappedBy = "monitorador")
    private List<Endereco> enderecos = new ArrayList<>();

    public Monitorador(){

    }

    public Monitorador(TipoPessoa tipoPessoa, String cpf, String cnpj, String nome, String razaoSocial, String email, String rg, String inscricaoEstadual, String dataNascimento, Boolean ativo) {
        this.tipoPessoa = tipoPessoa;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.email = email;
        this.rg = rg;
        this.inscricaoEstadual = inscricaoEstadual;
        this.data = data;
        this.ativo = ativo;
    }

    public void editar(Monitorador m) {
        this.tipoPessoa = m.tipoPessoa;
        this.cpf = m.cpf;
        this.cnpj = m.cnpj;
        this.nome = m.nome;
        this.razaoSocial = m.razaoSocial;
        this.email = m.email;
        this.rg = m.rg;
        this.inscricaoEstadual = m.inscricaoEstadual;
        this.data = m.data;
        this.ativo = m.ativo;
        this.enderecos = m.enderecos;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getCpf() {
        if (cpf == null)
            return null;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public void setCpf(String cpf) {
        if (cpf != null)
            this.cpf = cpf.replaceAll("[^0-9]", "");
    }

    public String getCnpj() {
        if (cnpj == null)
            return null;
        return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." +
                cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
    }

    public void setCnpj(String cnpj) {
        if (cnpj != null)
            this.cnpj = cnpj.replaceAll("[^0-9]", "");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isEmpty())
            this.nome = null;
        else
            this.nome = nome;

    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        if (razaoSocial == null || razaoSocial.isEmpty()) this.razaoSocial = null;
        else this.razaoSocial = razaoSocial;
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

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Monitorador{" +
                "tipoPessoa=" + tipoPessoa +
                ", cpf='" + cpf + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nome='" + nome + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                ", rg='" + rg + '\'' +
                ", inscricaoEstadual='" + inscricaoEstadual + '\'' +
                ", email='" + email + '\'' +
                ", data='" + data + '\'' +
                ", ativo=" + ativo +
                ", enderecos=" + enderecos +
                '}';
    }
}
