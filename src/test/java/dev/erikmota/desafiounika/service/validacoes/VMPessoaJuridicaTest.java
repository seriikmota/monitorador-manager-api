package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VMPessoaJuridicaTest {

    @Test
    @DisplayName("Retornar sucesso por pessoa física")
    void validarCase1() {
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.FISICA);
        m.setCnpj(null);
        m.setRazao(null);
        m.setInscricao(null);

        VMPessoaJuridica validacao = new VMPessoaJuridica();

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar sucesso por pessoa juridica, com cnpj, razao, inscricao")
    void validarCase2(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.JURIDICA);
        m.setCnpj("35.585.625/0001-47");
        m.setRazao("Pietro e Sueli Telas Ltda");
        m.setInscricao("312.017.341.216");

        VMPessoaJuridica validacao = new VMPessoaJuridica();

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar exception por pessoa juridica, sem cnpj")
    void validarCase3(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.JURIDICA);
        m.setCnpj(null);
        m.setRazao("Pietro e Sueli Telas Ltda");
        m.setInscricao("312.017.341.216");

        VMPessoaJuridica validacao = new VMPessoaJuridica();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Pessoa jurídica deve inserir o CNPJ!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por pessoa juridica, sem razao")
    void validarCase4(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.JURIDICA);
        m.setCnpj("35.585.625/0001-47");
        m.setRazao(null);
        m.setInscricao("312.017.341.216");

        VMPessoaJuridica validacao = new VMPessoaJuridica();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Pessoa jurídica deve inserir a Razao Social!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por pessoa juridica sem inscricao")
    void validarCase5(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.JURIDICA);
        m.setCnpj("35.585.625/0001-47");
        m.setRazao("Pietro e Sueli Telas Ltda");
        m.setInscricao(null);

        VMPessoaJuridica validacao = new VMPessoaJuridica();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Pessoa jurídica deve inserir a Inscrição Estadual!", exception.getMessage());
    }
}