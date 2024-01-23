package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VMPessoaFisicaTest {

    @Test
    @DisplayName("Retornar sucesso por pessoa juridica")
    void validarCase1() {
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.JURIDICA);
        m.setCpf(null);
        m.setNome(null);
        m.setRg(null);

        VMPessoaFisica validacao = new VMPessoaFisica();

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar sucesso por pessoa fisica, com cpf, nome, rg")
    void validarCase2(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.FISICA);
        m.setCpf("601.198.345-61");
        m.setNome("Levi Fábio Sales");
        m.setRg("41.105.006-0");

        VMPessoaFisica validacao = new VMPessoaFisica();

        assertDoesNotThrow(() -> validacao.validar(m));
    }

    @Test
    @DisplayName("Retornar exception por pessoa fisica, sem cpf")
    void validarCase3(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.FISICA);
        m.setCpf(null);
        m.setNome("Levi Fábio Sales");
        m.setRg("41.105.006-0");

        VMPessoaFisica validacao = new VMPessoaFisica();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Pessoa física deve inserir CPF!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por pessoa fisica, sem nome")
    void validarCase6(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.FISICA);
        m.setCpf("601.198.345-61");
        m.setNome(null);
        m.setRg("41.105.006-0");

        VMPessoaFisica validacao = new VMPessoaFisica();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Pessoa física deve inserir o Nome!", exception.getMessage());
    }

    @Test
    @DisplayName("Retornar exception por pessoa fisica, sem rg")
    void validarCase8(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.FISICA);
        m.setCpf("601.198.345-61");
        m.setNome("Levi Fábio Sales");
        m.setRg(null);

        VMPessoaFisica validacao = new VMPessoaFisica();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Pessoa física deve inserir o Rg!", exception.getMessage());
    }
}