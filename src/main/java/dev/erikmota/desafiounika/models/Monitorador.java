package dev.erikmota.desafiounika.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysql.cj.util.StringUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="monitorador")
public class Monitorador implements Comparable<Monitorador> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipo;
    @CPF
    private String cpf;
    private String nome;
    private String rg;
    @CNPJ
    private String cnpj;
    private String razao;
    private String inscricao;
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;
    private Boolean ativo;
    @OneToMany(mappedBy = "monitorador")
    private List<Endereco> enderecos = new ArrayList<>();

    public Monitorador() {}

    public Monitorador(Long id, TipoPessoa tipo, String cpf, String nome, String rg, String cnpj, String razao, String inscricao, String email, LocalDate data, Boolean ativo, List<Endereco> enderecos) {
        this.id = id;
        this.tipo = tipo;
        this.setCpf(cpf);
        this.nome = nome;
        this.rg = rg;
        this.setCnpj(cnpj);
        this.razao = razao;
        this.inscricao = inscricao;
        this.email = email;
        this.data = data;
        this.ativo = ativo;
        this.enderecos = enderecos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoPessoa getTipo() {
        return tipo;
    }

    public void setTipo(TipoPessoa tipo) {
        this.tipo = tipo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (!StringUtils.isEmptyOrWhitespaceOnly(cpf))
            this.cpf = cpf.replaceAll("[^0-9]", "");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        if (!StringUtils.isEmptyOrWhitespaceOnly(cnpj))
            this.cnpj = cnpj.replaceAll("[^0-9]", "");
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getInscricao() {
        return inscricao;
    }

    public void setInscricao(String inscricao) {
        this.inscricao = inscricao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public int compareTo(Monitorador m) {
        int comparacaoTipo = this.tipo.compareTo(m.tipo);
        if (comparacaoTipo != 0) {
            return comparacaoTipo;
        }
        if (this.tipo == TipoPessoa.FISICA) {
            return this.nome.compareTo(m.nome);
        } else {
            return this.razao.compareTo(m.razao);
        }
    }

    @JsonIgnore
    public String getNomeOrRazao() {
        if (tipo == TipoPessoa.FISICA)
            return nome;
        else
            return razao;
    }

    @Override
    public String toString() {
        return "\ntipo=" + tipo +
                ", \ncpf='" + cpf + '\'' +
                ", \nnome='" + nome + '\'' +
                ", \nrg='" + rg + '\'' +
                ", \ncnpj='" + cnpj + '\'' +
                ", \nrazao='" + razao + '\'' +
                ", \ninscricao='" + inscricao + '\'' +
                ", \nemail='" + email + '\'' +
                ", \ndata=" + data +
                ", \nativo=" + ativo + "\n";
    }
}
