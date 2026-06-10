package com.example.prototipo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "estados")
@Setter
@Getter
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uf;

    @ManyToMany
    @JoinTable(
            name = "termos_estados",
            joinColumns = @JoinColumn(name = "estado_id"),
            inverseJoinColumns = @JoinColumn(name = "term_id")
    )
    private List<SearchTerms> searchTerms;

    @OneToMany(mappedBy = "state")
    private List<Procurement> procurements;

    public State(){}

    public State(String uf){
        this.uf = uf;
    }
}
