package com.example.prototipo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Table(name = "customer_search_terms", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "term"})
})
public class SearchTerms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @NotBlank
    private String term;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Getter
    @NotNull
    private Customer customer;

    public SearchTerms(Customer customer, String term){
        this.term = term;
        this.customer = customer;
    }
}
