package com.example.prototipo.models;

import com.example.prototipo.records.OpportunitiesPNCP;
import com.example.prototipo.records.requests.ProcurementRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "procurement", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"customer_id", "pncpId"})
})
public class Procurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String pncpId;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private LocalDateTime openDate;

    @Getter
    @Setter
    private LocalDateTime closeDate;

    @Getter
    @Setter
    private String cnpj;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String uf;

    @Getter
    @Setter
    private String modalidade;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Getter
    @Setter
    private Customer customer;

    public Procurement(){}

    public Procurement(Customer customer, OpportunitiesPNCP opportunity){
        this.pncpId = opportunity.numero_controle_pncp();
        this.description = opportunity.description();
        this.city = opportunity.municipio_nome();
        this.openDate = opportunity.data_inicio_vigencia();
        this.closeDate = opportunity.data_fim_vigencia();
        this.cnpj = opportunity.orgao_cnpj();
        this.name = opportunity.orgao_nome();
        this.uf = opportunity.uf();
        this.modalidade = opportunity.modalidade_licitacao_nome();
        this.customer = customer;
    }
}