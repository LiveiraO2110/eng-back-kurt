package com.example.prototipo.controllers;

import com.example.prototipo.controller.ControllerSearchTerms;
import com.example.prototipo.exception.GlobalExceptionHandler;
import com.example.prototipo.models.Customer;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.service.SearchTermsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ControllerSearchTerms.class, GlobalExceptionHandler.class})
public class ControllerSearchTermsTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SearchTermsService service;

    private SearchTerms searchTerms;

    @BeforeEach
    void setup(){
        Customer customer = new Customer("Cliente Teste");
        searchTerms = new SearchTerms(customer, "Termo");
    }

    @Test
    @DisplayName("Deve retonar o termo quando não passar um atributo inválido")
    void shouldReturnCreateSearchTerm() throws Exception {
        String json = """
                {
                    "customerId": 1,
                    "term": "Termo",
                    "statesId": []
                }""";

        when(service.create(anyLong(), anyString(), anyList())).thenReturn(searchTerms);

        mockMvc.perform(post("/customers/search-terms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.term").value(searchTerms.getTerm()));
    }

    @Test
    @DisplayName("Deve retornar 400 quando enviar um termo vazio")
    void shouldReturnBadRequestWhenTermIsEmpty() throws Exception{
        String json = """
                {
                    "customerId": 1,
                    "term": "",
                    "statesId": []
                }""";

        mockMvc.perform(post("/customers/search-terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Termo não pode ser nulo ou vazio"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando enviar os estados nulos")
    void shouldReturnBadRequestWhenStateIsNull() throws Exception{
        String json = """
                {
                    "customerId": 1,
                    "term": "Termo",
                    "statesId": null
                }""";

        mockMvc.perform(post("/customers/search-terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Array do estados não pode ser nulo"));
    }
}
