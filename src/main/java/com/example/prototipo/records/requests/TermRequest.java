package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotBlank;

public record TermRequest(
        @NotBlank(message = "Termo não pode ser nulo ou vazio") String name
        ) {
}
