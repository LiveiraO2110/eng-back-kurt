package com.example.prototipo.repository;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.State;
import com.example.prototipo.records.OpportunitiesPNCP;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProcurementRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private ProcurementRepository repository;

    @Test
    void shouldReturnTrueExistsByCustomer_IdAndPncpid(){
        Customer customer = new Customer("Customer Test");
        em.persist(customer);

        State state = new State("UF");
        em.persist(state);

        Procurement procurement = new Procurement(customer, new OpportunitiesPNCP(
                "Titulo",
                "desc",
                "83024240000153-1-000084/2026",
                "Orgao",
                "Nome",
                "Municipio",
                "Uf",
                "Modalidade",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        ), state);
        procurement.setEditalLink("Link");
        em.persist(procurement);

        boolean result = repository.existsByCustomer_IdAndPncpId(customer.getId(), procurement.getPncpId());

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseExistsByCustomer_IdAndPncpid(){
        Customer customer = new Customer("Customer Test");
        em.persist(customer);

        State state = new State("UF");
        em.persist(state);

        Procurement procurement = new Procurement(customer, new OpportunitiesPNCP(
                "Titulo",
                "desc",
                "83024240000153-1-000084/2026",
                "Orgao",
                "Nome",
                "Municipio",
                "Uf",
                "Modalidade",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        ), state);
        procurement.setEditalLink("Link");
        em.persist(procurement);

        em.flush();

        boolean result = repository.existsByCustomer_IdAndPncpId(customer.getId(), "83024240000153-1-000084/2025");

        assertFalse(result);
    }
}
