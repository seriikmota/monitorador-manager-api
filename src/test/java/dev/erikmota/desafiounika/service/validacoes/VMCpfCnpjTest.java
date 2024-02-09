package dev.erikmota.desafiounika.service.validacoes;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.models.TipoPessoa;
import dev.erikmota.desafiounika.service.exceptions.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VMCpfCnpjTest {
    @Test
    @DisplayName("Retornar erro por não ter tipo de pessoa")
    void validarCase1(){
        Monitorador m = new Monitorador();
        m.setTipo(null);
        m.setCpf("453.881.398-24");
        m.setCnpj("72.943.057/0001-35");

        VMCpfCnpj validacao = new VMCpfCnpj();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("O Tipo da Pessoa é obrigatório!", exception.getMessage());
    }
    @Test
    @DisplayName("Retornar sucesso por pessoa fisica e cpf valido")
    void validarCase2(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.FISICA);
        m.setCpf("223.093.640-96");
        m.setCnpj("11.222.333/0001-44");

        VMCpfCnpj validacao = new VMCpfCnpj();

        assertDoesNotThrow(() -> validacao.validar(m));
    }
    @Test
    @DisplayName("Retornar erro por pessoa fisica e cpf invalido")
    void validarCase3(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.FISICA);
        m.setCpf("111.222.333-44");
        m.setCnpj("87.347.236/0001-87");

        VMCpfCnpj validacao = new VMCpfCnpj();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Esse CPF é inválido!", exception.getMessage());
    }
    @Test
    @DisplayName("Retornar sucesso por pessoa juridica e cnpj valido")
    void validarCase4(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.JURIDICA);
        m.setCpf("111.222.333-44");
        m.setCnpj("87.347.236/0001-87");

        VMCpfCnpj validacao = new VMCpfCnpj();

        assertDoesNotThrow(() -> validacao.validar(m));
    }
    @Test
    @DisplayName("Retornar erro por pessoa juridica e cnpj invalido")
    void validarCase5(){
        Monitorador m = new Monitorador();
        m.setTipo(TipoPessoa.JURIDICA);
        m.setCpf("223.093.640-96");
        m.setCnpj("11.222.333/0001-44");

        VMCpfCnpj validacao = new VMCpfCnpj();

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> validacao.validar(m));
        assertEquals("Esse CNPJ é inválido!", exception.getMessage());
    }

}