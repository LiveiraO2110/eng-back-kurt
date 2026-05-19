package com.example.prototipo.repository;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.SearchTerms;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SearchTermsRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private SearchTermsRepository repository;

    @Test
    void shouldReturnFindByTermAndCustomer_Id(){
        Customer customer = new Customer("Cliente Exemplo");
        em.persist(customer);
        SearchTerms searchTerms = new SearchTerms(customer, "Termo");
        em.persist(searchTerms);

        Optional<SearchTerms> result = repository.findByTermAndCustomer_Id("Termo", customer.getId());

        assertTrue(result.isPresent());
    }
}