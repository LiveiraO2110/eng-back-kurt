package com.example.prototipo.records.response;

import com.example.prototipo.models.Procurement;

import java.time.LocalDateTime;

public record ProcurementDTO(
    Long id,
    Long customerId,
    String customer,
    String pncpId,
    String description,
    String city,
    String uf,
    LocalDateTime openDate,
    LocalDateTime closeDate,
    String cnpj,
    String name,
    String modalidade
) {
    public ProcurementDTO(Procurement procurement){
        this(
                procurement.getId(),
                procurement.getCustomer().getId(),
                procurement.getCustomer().getName(),
                procurement.getPncpId(),
                procurement.getDescription(),
                procurement.getCity(),
                procurement.getUf(),
                procurement.getOpenDate(),
                procurement.getCloseDate(),
                procurement.getCnpj(),
                procurement.getName(),
                procurement.getModalidade()
        );
    }
}
