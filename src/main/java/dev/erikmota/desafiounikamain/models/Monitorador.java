package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="monitorador")
public class Monitorador {
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo da pessoa é obrigatório!")
    private TipoPessoa tipo;
    @CPF
    private String cpf;
    @CNPJ
    private String cnpj;
    private String nome;
    private String razao;
    private String rg;
    private String inscricao;
    @NotBlank(message = "O email é obrigatório!")
    private String email;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "A data é obrigatória!")
    private LocalDate data;
    @NotNull(message = "O campo ativo é obrigatório!")
    private Boolean ativo;
    @OneToMany(mappedBy = "monitorador")
    private List<Endereco> enderecos = new ArrayList<>();

    public Monitorador(){

    }

    public Monitorador(TipoPessoa tipo, String cnpj, String razao, String inscricao, String cpf, String nome, String rg, LocalDate data, String email, Boolean ativo) {
        this.tipo = tipo;
        setCpf(cpf);
        setCnpj(cnpj);
        this.nome = nome;
        this.razao = razao;
        this.email = email;
        this.rg = rg;
        this.inscricao = inscricao;
        this.data = data;
        this.ativo = ativo;
    }

    public void editar(Monitorador m) {
        this.tipo = m.tipo;
        this.cpf = m.cpf;
        this.cnpj = m.cnpj;
        this.nome = m.nome;
        this.razao = m.razao;
        this.email = m.email;
        this.rg = m.rg;
        this.inscricao = m.inscricao;
        this.data = m.data;
        this.ativo = m.ativo;
        this.enderecos = m.enderecos;
    }

    public TipoPessoa getTipo() {
        return tipo;
    }

    public void setTipo(TipoPessoa tipo) {
        this.tipo = tipo;
    }

    public String getCpf() {
        if (cpf != null && cpf.length() == 11)
            return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
        else
            return null;
    }

    public void setCpf(String cpf) {
        if (cpf != null)
            this.cpf = cpf.replaceAll("[^0-9]", "");
    }

    public String getCnpj() {
        if (cnpj != null && cnpj.length() == 14)
            return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." +
                    cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
        else
            return null;
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

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        if (razao == null || razao.isEmpty()) this.razao = null;
        else this.razao = razao;
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

    public String getInscricao() {
        return inscricao;
    }

    public void setInscricao(String inscricao) {
        this.inscricao = inscricao;
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
                "tipo=" + tipo +
                ", cpf='" + cpf + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", nome='" + nome + '\'' +
                ", razao='" + razao + '\'' +
                ", rg='" + rg + '\'' +
                ", inscricao='" + inscricao + '\'' +
                ", email='" + email + '\'' +
                ", data='" + data + '\'' +
                ", ativo=" + ativo +
                ", enderecos=" + enderecos +
                '}';
    }
}
