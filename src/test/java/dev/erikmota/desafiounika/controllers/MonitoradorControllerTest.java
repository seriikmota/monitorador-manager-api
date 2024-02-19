package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Monitorador;
import dev.erikmota.desafiounika.service.MonitoradorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MonitoradorControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MonitoradorService service;
    @Mock
    private Monitorador m;

    @Test
    void cadastrarCase1() throws Exception {
        when(m.isNull()).thenReturn(false);
        String json = """
                {
                    "tipo": "JURIDICA",
                    "cnpj": "73.061.355/0001-64",
                    "razao": "Martin e Carlos Corretores Associados ME",
                    "inscricao": "200292430079",
                    "cpf": null,
                    "nome": null,
                    "rg": null,
                    "email": "ti@martinecarloscorretoresassociadosme.com.br",
                    "data": "01/10/2019",
                    "ativo": true
                }
                """;

        mvc.perform(post("/monitorador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isCreated());
        verify(service, times(1)).cadastrar(any());
    }

    @Test
    void cadastrarCase2() throws Exception {
        when(m.isNull()).thenReturn(false);
        String json = "";

        mvc.perform(post("/monitorador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any());
    }

    @Test
    void cadastrarCase3() throws Exception {
        when(m.isNull()).thenReturn(false);
        String json = "{}";

        mvc.perform(post("/monitorador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any());
    }

    @Test
    void cadastrarCase4() throws Exception {
        when(m.isNull()).thenReturn(true);
        String json = "{}";

        mvc.perform(post("/monitorador")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any());
    }

    @Test
    void editarCase1() throws Exception {
        when(m.isNull()).thenReturn(false);
        String json = """
                {
                  "tipo": "JURIDICA",
                  "cnpj": "73.061.355/0001-64",
                  "razao": "Martin e Carlos Corretores Associados ME",
                  "inscricao": "200292430079",
                  "cpf": null,
                  "nome": null,
                  "rg": null,
                  "email": "ti@martinecarloscorretoresassociadosme.com.br",
                  "data": "01/10/2019",
                  "ativo": true
                }
                """;

        mvc.perform(put("/monitorador/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());
        verify(service, times(1)).editar(any(), any());
    }

    @Test
    void editarCase2() throws Exception {
        when(m.isNull()).thenReturn(true);
        String json = "{}";

        mvc.perform(put("/monitorador/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isBadRequest());
        verify(service, never()).editar(any(), any());
    }

    @Test
    void excluir() throws Exception {
        mvc.perform(delete("/monitorador/1"))
                .andExpect(status().isOk());
        verify(service, times(1)).excluir(any());
    }

    @Test
    void listar() throws Exception {
        mvc.perform(get("/monitorador"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(service, times(1)).listar();
    }

    @Test
    void filtrar() throws Exception {
        mvc.perform(get("/monitorador/filtrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(service, times(1)).filtrar(any(), any(), any());
    }

    @Test
    void relatorioPdf() throws Exception {
        byte[] relatorioBytesMock = "Conteúdo do relatório PDF".getBytes();
        when(service.gerarRelatorioPdf(null, null, null, null)).thenReturn(relatorioBytesMock);

        mvc.perform(get("/monitorador/pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
        verify(service, times(1)).gerarRelatorioPdf(null, null, null, null);
    }

    @Test
    void relatorioExcel() throws Exception {
        byte[] relatorioBytesMock = "Conteúdo do relatório EXCEL".getBytes();
        when(service.gerarRelatorioExcel(null, null, null, null)).thenReturn(relatorioBytesMock);

        var response = mvc.perform(get("/monitorador/excel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn().getResponse();
        assertTrue(Objects.requireNonNull(response.getHeader(HttpHeaders.CONTENT_DISPOSITION)).endsWith(".xlsx\""));
        verify(service, times(1)).gerarRelatorioExcel(null, null, null, null);
    }

    @Test
    void importar() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.xlsx", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        mvc.perform(multipart("/monitorador/importar")
                .file(file))
                .andExpect(status().isOk());
        verify(service, times(1)).importar(file);
    }

    @Test
    void modelo() throws Exception {
        byte[] relatorioBytesMock = "Conteúdo do modelo de importação EXCEL".getBytes();
        when(service.gerarModelo()).thenReturn(relatorioBytesMock);

        var response = mvc.perform(get("/monitorador/importar/modelo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn().getResponse();
        assertTrue(Objects.requireNonNull(response.getHeader(HttpHeaders.CONTENT_DISPOSITION)).endsWith(".xlsx\""));
        verify(service, times(1)).gerarModelo();
    }
}