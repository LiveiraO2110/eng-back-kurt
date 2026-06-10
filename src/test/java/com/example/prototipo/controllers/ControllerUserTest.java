package com.example.prototipo.controllers;

import com.example.prototipo.controller.ControllerUser;
import com.example.prototipo.exception.GlobalExceptionHandler;
import com.example.prototipo.models.User;
import com.example.prototipo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ControllerUser.class, GlobalExceptionHandler.class})
public class ControllerUserTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Test
    @DisplayName("Deve criar usuário quando os parametros corretos forem passados")
    void shouldReturnCreateUser() throws Exception {
        String json = """
                {
                    "name":"User Teste"
                }
                """;

        User user = new User("User Teste");
        user.setId(1L);

        when(service.createUser(anyString())).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(user.getName()));
    }

    @Test
    @DisplayName("Deve retornar Bad Request quando o nome for nulo/vazio")
    void shouldReturnBadRequestNameIsBlank() throws Exception{
        String json = """
                {
                    "name":""
                }
                """;

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Nome de usuário não pode ser nulo ou vazio"));
    }
}
