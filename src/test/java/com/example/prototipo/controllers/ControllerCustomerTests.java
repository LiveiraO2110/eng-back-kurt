package com.example.prototipo.controllers;

import com.example.prototipo.controller.ControllerCustomer;
import com.example.prototipo.exception.GlobalExceptionHandler;
import com.example.prototipo.models.Customer;
import com.example.prototipo.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ControllerCustomer.class, GlobalExceptionHandler.class})
public class ControllerCustomerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService service;

    @Test
    void shouldReturnCreatedCustomer() throws Exception{
        Customer customer = new Customer("Cliente");
        String json = """
                {
                    "userId": 1,
                    "name": "Cliente"
                }""";

        when(service.createCustomer(anyLong(), anyString())).thenReturn(customer);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(customer.getName()));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsEmpty() throws Exception{
        String json = """
                {
                    "userId": 1,
                    "name": ""
                }""";

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Nome do cliente não pode ser nulo ou vazio"));
    }
}
