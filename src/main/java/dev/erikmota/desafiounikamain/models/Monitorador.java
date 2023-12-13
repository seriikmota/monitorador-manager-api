package dev.erikmota.desafiounikamain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Monitorador {
    //Atributos
    private Long id;
    private TipoPessoa tipoPessoa;
    private String cpf;
    private String cnpj;
    private String nome;
    private String razaoSocial;
    private String email;
    private String rg;
    private Long inscricaoSocial;
    private Date dataNascimento;
    private String ativo;
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

}
