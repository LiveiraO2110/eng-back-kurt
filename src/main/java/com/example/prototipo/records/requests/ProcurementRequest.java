package com.example.prototipo.records.requests;

import com.example.prototipo.records.OpportunitiesPNCP;
import jakarta.validation.constraints.NotNull;

public record ProcurementRequest(
    @NotNull(message = "Id do cliente não pode ser null") Long customerId,
    @NotNull(message = "O objeto do edital não pode ser nulo") OpportunitiesPNCP procurement
) {
}
