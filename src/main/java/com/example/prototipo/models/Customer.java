package com.example.prototipo.models;

import com.example.prototipo.records.requests.CustomerRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @OneToMany(mappedBy = "customer")
    @Getter
    private final Set<SearchTerms> searchTerms = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Getter
    private User user;

    @OneToMany(mappedBy = "customer")
    private final List<Procurement> procurements = new ArrayList<>();

    public Customer(){}

    public Customer(String name){
        this.name = name;
    }

    public Customer(User user, CustomerRequest customerRequest){
        this.user = user;
        this.name = customerRequest.name();
    }

    public Customer(User user, String name, Set<SearchTerms> searchTerms){
        this.user = user;
        this.name = name;
        this.searchTerms.addAll(searchTerms);
    }

    public void addSearchTerm(SearchTerms term){
        searchTerms.add(term);
    }
}
