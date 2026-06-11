package com.example.prototipo.records.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record SearchTermsRequest (
        @NotNull(message = "Id do cliente não pode ser nulo") Long customerId,
        @NotEmpty(message = "O array com os termos não pode ser nulo") Set<SearchTermBody> terms
){
}
