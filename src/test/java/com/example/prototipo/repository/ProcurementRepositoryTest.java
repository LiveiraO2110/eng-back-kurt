package com.example.prototipo.repository;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        Procurement procurement = new Procurement();
        procurement.setPncpId("83024240000153-1-000084/2026");
        procurement.setCustomer(customer);

        em.persist(procurement);

        boolean result = repository.existsByCustomer_IdAndPncpId(customer.getId(), procurement.getPncpId());

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseExistsByCustomer_IdAndPncpid(){
        Customer customer = new Customer("Customer Test");
        em.persist(customer);
        Procurement procurement = new Procurement();
        procurement.setPncpId("83024240000153-1-000084/2026");
        procurement.setCustomer(customer);

        em.persist(procurement);

        boolean result = repository.existsByCustomer_IdAndPncpId(customer.getId(), "83024240000153-1-000084/2025");

        assertFalse(result);
    }
}
