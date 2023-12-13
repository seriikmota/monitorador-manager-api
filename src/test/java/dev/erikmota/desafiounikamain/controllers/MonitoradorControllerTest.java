package dev.erikmota.desafiounikamain.controllers;

import dev.erikmota.desafiounikamain.models.Monitorador;
import dev.erikmota.desafiounikamain.models.TipoPessoa;
import dev.erikmota.desafiounikamain.repository.MonitoradorRepository;
import dev.erikmota.desafiounikamain.service.MonitoradorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoradorControllerTest {

    @MockBean
    private MonitoradorService service;

    @Autowired
    private MockMvc mvc;

    @Test
    void testCadastro() throws Exception {

        String json = """
                {
                    "tipo": "Física",
                    "cpf": "71061170136",
                    "cnpj": "null",
                    "nome": "Erik de Sousa",
                    "razao_social": "null",
                    "email": "seriikmota.dev@gmail.com",
                    "rg": "7118387",
                    "inscricao_social": "123456",
                    "data_nascimento": "2003-09-01",
                    "ativo": "Sim"
                }
                """;

        MockHttpServletResponse response = mvc.perform(
                post("/monitorador")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        Assertions.assertEquals(200, response.getStatus());
    }

}