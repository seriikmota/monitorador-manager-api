package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.service.MonitoradorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoradorControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MonitoradorService service;

    @Test
    void cadastrarCase1() throws Exception {
        String json = "";

        var response = mvc.perform(
                post("/monitorador")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(409, response.getStatus());
    }

    @Test
    void cadastrarCase2() throws Exception {
        String json = """
                {
                  "tipo": "FISICA"
                }
                """;

        var response = mvc.perform(
                post("/monitorador")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(201, response.getStatus());
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
    void importar() {
    }

    @Test
    void modelo() {
    }
}