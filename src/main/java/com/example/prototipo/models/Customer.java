package com.example.prototipo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<SearchTerms> searchTerms = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @OneToMany(mappedBy = "customer")
    private final Set<Procurement> procurements = new HashSet<>();

    public Customer(){}

    public Customer(String name){
        this.name = name;
    }

    public Customer(User user, String name){
        this.user = user;
        this.name = name;
    }

    public Customer(User user, String name, Set<SearchTerms> searchTerms){
        this.user = user;
        this.name = name;
        this.searchTerms.addAll(searchTerms);
    }

    public void addSearchTerm(SearchTerms term){
        searchTerms.add(term);
    }

    public void removeSearchTerm(SearchTerms terms){
        this.searchTerms.remove(terms);
        terms.setCustomer(null);
    }
}
