package com.example.prototipo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
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

    @ElementCollection
    @Getter
    private final Set<String> searchTerms = new HashSet<>();

    public Customer(){}

    public Customer(String name){
        this.name = name;
    }
}
