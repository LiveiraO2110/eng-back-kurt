package com.example.prototipo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<Customer> customers =  new ArrayList<>();

    public User(){}

    public User(String name){
        this.name = name;
    }
}
