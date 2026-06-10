package com.example.prototipo.repository;

import com.example.prototipo.models.State;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class StateRepositoryTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private StateRepository repository;

    @Test
    void shouldReturnStateByUf(){
        State state = new State("RS");
        State state1 = new State("SC");

        em.persist(state);
        em.persist(state1);

        Optional<State> result = repository.findByUf(state.getUf());

        assertFalse(result.isEmpty());
        assertEquals(result, Optional.of(state));
    }
}
