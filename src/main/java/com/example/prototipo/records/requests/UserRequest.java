package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String name
) {
}
