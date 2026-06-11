package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record SearchTermBody(
        @NotBlank(message = "Termo não pode ser nulo ou vazio") String term,
        @NotNull(message = "Array do estados não pode ser nulo") Set<Long> statesId
) {
}