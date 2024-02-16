package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.service.EnderecoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class EnderecoControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EnderecoService service;

    @Test
    void cadastrar() {
    }

    @Test
    void editar() {
    }

    @Test
    void excluir() {
    }

    @Test
    void listar() {
    }

    @Test
    void filtrar() {
    }

    @Test
    void relatorioPdf() {
    }

    @Test
    void relatorioExcel() {
    }

    @Test
    void buscarCep() {
    }
}