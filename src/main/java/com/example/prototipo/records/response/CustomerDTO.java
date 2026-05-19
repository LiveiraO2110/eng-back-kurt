package com.example.prototipo.records.response;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.SearchTerms;

import java.util.Set;
import java.util.stream.Collectors;

public record CustomerDTO(
        Long id,
        String name,
        Set<SearchTermDTO> searchTerms
) {
    public CustomerDTO(Customer customer){
        this(
                customer.getId(),
                customer.getName(),
                customer.getSearchTerms().stream().map(SearchTermDTO::new).collect(Collectors.toSet())
        );
    }
}
