package com.example.prototipo.services;


import com.example.prototipo.exception.BusinessException;
import com.example.prototipo.models.Customer;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import com.example.prototipo.service.SearchTermsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchTermsServiceTest {
    @Mock
    private SearchTermsRepository repository;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private SearchTermsService service;

    @Test
    @DisplayName("Deve excluir e retirar o termo, quando o termo pertencer ao cliente selecionado")
    void shouldDeleteTermByCustomerIdAndTermId(){
        Customer customer = new Customer("Cliente Test");
        SearchTerms terms = new SearchTerms(customer, "Termo Teste");
        customer.addSearchTerm(terms);

        when(repository.findById(1L)).thenReturn(Optional.of(terms));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertEquals(1, customer.getSearchTerms().size());

        Set<SearchTerms> result = service.deleteById(1L, 1L);

        assertEquals(0, result.size());
        assertEquals(0, customer.getSearchTerms().size());
    }

    @Test
    void shouldThrowBusinessExceptionWhenCustomerNotHasTerm(){
        Customer customer = new Customer("Cliente Test");
        SearchTerms terms = new SearchTerms(customer, "Termo Teste");
        SearchTerms terms1 = new SearchTerms(customer, "Outro Termo Teste");
        customer.addSearchTerm(terms1);

        when(repository.findById(1L)).thenReturn(Optional.of(terms));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertEquals(1, customer.getSearchTerms().size());

        assertThrows(BusinessException.class, () -> service.deleteById(1L, 1L));
        verify(repository, times(0)).delete(terms);
    }

}