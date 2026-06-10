package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SearchTermRequest(
        @NotNull(message = "Id do cliente não pode ser nulo") Long customerId,
        @NotBlank(message = "Termo não pode ser nulo ou vazio") String term,
        @NotNull(message = "Array do estados não pode ser nulo") List<Long> statesId
) {
}