package dev.erikmota.desafiounikamain.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.erikmota.desafiounikamain.service.ValidacaoException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
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
    @NotBlank
    private String email;
    @NotBlank
    private String rg;
    @NotNull
    @Column(name = "inscricao_social")
    private Long inscricaoSocial;
    @NotBlank
    @Column(name = "data_nascimento")
    private String dataNascimento;
    @NotBlank
    private String ativo;
    @OneToMany(mappedBy = "monitorador")
    private List<Endereco> enderecos = new ArrayList<>();

    public Monitorador(){

    }

    public Monitorador(TipoPessoa tipoPessoa, String cpf, String cnpj, String nome, String razaoSocial, String email, String rg, Long inscricaoSocial, String dataNascimento, String ativo) {
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

    public Long getId() {
        return id;
    }

}
