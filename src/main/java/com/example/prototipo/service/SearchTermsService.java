package com.example.prototipo.service;

import com.example.prototipo.exception.BusinessException;
import com.example.prototipo.models.Customer;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.models.State;
import com.example.prototipo.records.requests.SearchTermBody;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import com.example.prototipo.repository.StateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchTermsService {
    @Autowired
    private SearchTermsRepository repository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StateRepository stateRepository;

    public List<SearchTerms> getAll(){
        return repository.findAll();
    }

    public SearchTerms getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Termo de busca não encontrado"));
    }

    @Transactional
    public Set<SearchTerms> create(Long customerId, Set<SearchTermBody> terms){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        Set<String> termsString = customer.getSearchTerms().stream().map((t) -> t.getTerm().toLowerCase()).collect(Collectors.toSet());

        for (SearchTermBody term : terms) {
            if(termsString.contains(term.term().toLowerCase())){
                continue;
            }

            SearchTerms searchTerm = new SearchTerms(customer, term.term());

            searchTerm = repository.save(searchTerm);

            if(!term.statesId().isEmpty()){
                for (Long id : term.statesId()) {
                    State state = stateRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado"));
                    searchTerm.addState(state);
                }
            }

            customer.addSearchTerm(searchTerm);
        }

        return customer.getSearchTerms();
    }

    public Set<SearchTerms> deleteById(Long customerId, Long termId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        SearchTerms term = repository.findById(termId)
                .orElseThrow(() -> new EntityNotFoundException("Termo não encontrado"));

        if(customer.getSearchTerms() == null || !customer.getSearchTerms().contains(term)){
            throw new BusinessException("O termo não pertence ao cliente");
        }

        customer.removeSearchTerm(term);

        repository.delete(term);

        return customer.getSearchTerms() == null  || customer.getSearchTerms().isEmpty() ? Set.of() : customer.getSearchTerms();
    }
}