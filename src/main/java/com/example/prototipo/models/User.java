package com.example.prototipo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lawyer")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @NotBlank
    @Setter
    @Getter
    private String name;

    @OneToMany(mappedBy = "lawyer")
    @Getter
    private List<Customer> customers =  new ArrayList<>();

    public User(){}

    public User(String name){
        this.name = name;
    }
}
