package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotNull Long user_id,
        @NotBlank String name
) {
}
