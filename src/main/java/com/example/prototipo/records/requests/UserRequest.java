package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Nome de usuário não pode ser nulo ou vazio") String name
) {
}
