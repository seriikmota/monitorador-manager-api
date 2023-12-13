package dev.erikmota.desafiounikamain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotBlank
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoPessoa tipoPessoa;
    private String cpf;
    private String cnpj;
    private String nome;
    @Column(name = "razao_social")
    private String razaoSocial;
    @NotBlank
    private String email;
    @NotBlank
    private String rg;
    @NotBlank
    @Column(name = "inscricao_social")
    private Long inscricaoSocial;
    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascimento")
    private Date dataNascimento;
    @NotBlank
    private String ativo;
    @OneToMany(mappedBy = "monitorador")
    private List<Endereco> enderecos = new ArrayList<>();

    public Monitorador(){

    }

    public Monitorador(TipoPessoa tipoPessoa, String cpf, String cnpj, String nome, String razaoSocial, String email, String rg, Long inscricaoSocial, Date dataNascimento, String ativo, List<Endereco> enderecos) {
        this.tipoPessoa = tipoPessoa;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.email = email;
        this.rg = rg;
        this.inscricaoSocial = inscricaoSocial;
        this.dataNascimento = dataNascimento;
        this.ativo = ativo;
        this.enderecos = enderecos;
    }

    public void editar(Monitorador m) {
        this.tipoPessoa = m.tipoPessoa;
        this.cpf = m.cpf;
        this.cnpj = m.cnpj;
        this.nome = m.nome;
        this.razaoSocial = m.razaoSocial;
        this.email = m.email;
        this.rg = m.rg;
        this.inscricaoSocial = m.inscricaoSocial;
        this.dataNascimento = m.dataNascimento;
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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
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

    public Long getId() {
        return id;
    }
}
