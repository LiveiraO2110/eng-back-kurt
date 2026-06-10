package com.example.prototipo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "termos_de_busca", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cliente_id", "termo"})
})
@Getter
@Setter
public class SearchTerms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "termo", nullable = false)
    private String term;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @NotNull
    private Customer customer;

    @ManyToMany(mappedBy = "searchTerms")
    private Set<State> states = new HashSet<>();

    public SearchTerms(){}

    public SearchTerms(Customer customer, String term){
        this.term = term;
        this.customer = customer;
    }

    public void addState(State state){
        this.states.add(state);
    }
}
