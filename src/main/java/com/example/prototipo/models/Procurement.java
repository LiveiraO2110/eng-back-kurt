package com.example.prototipo.models;

import com.example.prototipo.enums.Status;
import com.example.prototipo.records.OpportunitiesPNCP;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "editais", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cliente_id", "pncpId"})
})
@Getter
@Setter
public class Procurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pncpId;

    @Lob
    @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "cidade", nullable = false)
    private String city;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime openDate;

    @Column(name = "data_fechamento", nullable = false)
    private LocalDateTime closeDate;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "modalidade", nullable = false)
    private String modalidade;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "numero_sequencial", nullable = false)
    private String numeroSequencial;

    @Column(name = "edital_link", nullable = false)
    private String editalLink;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private State state;

    public Procurement(){}

    public Procurement(Customer customer, OpportunitiesPNCP opportunity, State state){
        this.pncpId = opportunity.numero_controle_pncp();
        this.description = opportunity.description();
        this.city = opportunity.municipio_nome();
        this.openDate = opportunity.data_inicio_vigencia();
        this.closeDate = opportunity.data_fim_vigencia();
        this.cnpj = opportunity.orgao_cnpj();
        this.name = opportunity.orgao_nome();
        this.modalidade = opportunity.modalidade_licitacao_nome();
        this.numeroSequencial = opportunity.numero_sequencial();
        this.customer = customer;
        this.state = state;
        this.status = Status.PENDENTE;
    }

    @Override
    public String toString() {
        return "Procurement{" +
                "id=" + getId() +
                ", pncpId='" + pncpId + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", openDate=" + openDate +
                ", closeDate=" + closeDate +
                ", cnpj='" + cnpj + '\'' +
                ", name='" + name + '\'' +
                ", uf='" + state.getUf() + '\'' +
                ", modalidade='" + modalidade + '\'' +
                ", validated=" + status.toString() +
                ", customer=" + customer +
                '}';
    }
}