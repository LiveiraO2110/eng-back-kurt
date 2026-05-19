package com.example.prototipo.records.requests;

import com.example.prototipo.records.OpportunitiesPNCP;

public record ProcurementRequest(
    Long customerId,
    OpportunitiesPNCP procurement
) {
}
