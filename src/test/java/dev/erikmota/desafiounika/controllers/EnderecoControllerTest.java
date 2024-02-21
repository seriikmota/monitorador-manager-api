/*package dev.erikmota.desafiounika.controllers;

import dev.erikmota.desafiounika.models.Endereco;
import dev.erikmota.desafiounika.service.EnderecoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EnderecoControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private EnderecoService service;
    @Mock
    private Endereco e;

    @Test
    void cadastrarCase1() throws Exception {
        when(e.isNull()).thenReturn(false);
        String json = """
                {
                  "cep": "29161738",
                  "endereco": "Rua Castelo",
                  "numero": "949",
                  "bairro": "Jardim Carapina",
                  "cidade": "Serra",
                  "estado": "ES",
                  "telefone": "27988260349",
                  "principal": true
                }
                """;

        mvc.perform(post("/endereco?monitoradorId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        verify(service, times(1)).cadastrar(any(), any());
    }

    @Test
    void cadastrarCase2() throws Exception {
        when(e.isNull()).thenReturn(false);
        String json = """
                {
                  "cep": "29161738",
                  "endereco": "Rua Castelo",
                  "numero": "949",
                  "bairro": "Jardim Carapina",
                  "cidade": "Serra",
                  "estado": "ES",
                  "telefone": "27988260349",
                  "principal": true
                }
                """;

        mvc.perform(post("/endereco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any(), any());
    }

    @Test
    void cadastrarCase3() throws Exception {
        when(e.isNull()).thenReturn(false);
        String json = "";

        mvc.perform(post("/endereco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any(), any());
    }

    @Test
    void cadastrarCase4() throws Exception {
        when(e.isNull()).thenReturn(false);
        String json = "{}";

        mvc.perform(post("/endereco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any(), any());
    }

    @Test
    void cadastrarCase5() throws Exception {
        when(e.isNull()).thenReturn(true);
        String json = "{}";

        mvc.perform(post("/endereco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).cadastrar(any(), any());
    }

    @Test
    void editarCase1() throws Exception {
        when(e.isNull()).thenReturn(false);
        String json = """
                {
                  "cep": "29161738",
                  "endereco": "Rua Castelo",
                  "numero": "949",
                  "bairro": "Jardim Carapina",
                  "cidade": "Serra",
                  "estado": "ES",
                  "telefone": "27988260349",
                  "principal": true
                }
                """;

        mvc.perform(put("/endereco/1?monitoradorId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        verify(service, times(1)).editar(any(), any(), any());
    }

    @Test
    void editarCase2() throws Exception {
        when(e.isNull()).thenReturn(true);
        String json = "{}";

        mvc.perform(put("/endereco/1?monitoradorId=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).editar(any(), any(), any());
    }

    @Test
    void editarCase3() throws Exception {
        when(e.isNull()).thenReturn(false);
        String json = "{}";

        mvc.perform(put("/endereco/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).editar(any(), any(), any());
    }

    @Test
    void excluirCase1() throws Exception {
        mvc.perform(delete("/endereco/1"))
                .andExpect(status().isOk());
        verify(service, times(1)).excluir(any());
    }

    @Test
    void excluirCase2() throws Exception {
        mvc.perform(delete("/endereco"))
                .andExpect(status().isBadRequest());
        verify(service, never()).excluir(any());
    }

    @Test
    void listar() throws Exception {
        mvc.perform(get("/endereco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(service, times(1)).listar();
    }

    @Test
    void filtrar() throws Exception {
        mvc.perform(get("/endereco/filtrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(service, times(1)).filtrar(any(), any(), any(), any());
    }

    @Test
    void relatorioPdf() throws Exception {
        byte[] relatorioBytesMock = "Conteúdo do relatório PDF".getBytes();
        when(service.gerarRelatorioPdf(null, null, null, null, null)).thenReturn(relatorioBytesMock);

        mvc.perform(get("/endereco/pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
        verify(service, times(1)).gerarRelatorioPdf(null, null, null, null, null);
    }

    @Test
    void relatorioExcel() throws Exception {
        byte[] relatorioBytesMock = "Conteúdo do relatório EXCEL".getBytes();
        when(service.gerarRelatorioExcel(null, null, null, null, null)).thenReturn(relatorioBytesMock);

        var response = mvc.perform(get("/endereco/excel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andReturn().getResponse();
        assertTrue(Objects.requireNonNull(response.getHeader(HttpHeaders.CONTENT_DISPOSITION)).endsWith(".xlsx\""));
        verify(service, times(1)).gerarRelatorioExcel(null, null, null, null, null);
    }

    @Test
    void buscarCepCase1() throws Exception {
        mvc.perform(get("/endereco/cep/69082644"))
                .andExpect(status().isOk());
        verify(service, times(1)).buscarCep(any());
    }

    @Test
    void buscarCepCase2() throws Exception {
        mvc.perform(get("/endereco/cep/"))
                .andExpect(status().isNotFound());
        verify(service, never()).buscarCep(any());
    }
}*/