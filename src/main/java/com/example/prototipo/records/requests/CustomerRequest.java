package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotNull(message = "Id do usuário não pode ser nulo") Long userId,
        @NotBlank(message = "Nome do cliente não pode ser nulo ou vazio") String name
) {
}
