package com.example.prototipo.records.response;

import com.example.prototipo.models.SearchTerms;

public record SearchTermDTO (
        Long id,
        String term,
        Long customerId
) {
    public SearchTermDTO(SearchTerms searchTerm){
        this(
                searchTerm.getId(),
                searchTerm.getTerm(),
                searchTerm.getCustomer().getId()
        );
    }
}
