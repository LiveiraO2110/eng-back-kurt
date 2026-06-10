package com.example.prototipo.service;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.models.State;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import com.example.prototipo.repository.StateRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public SearchTerms create(Long customerId, String term, List<Long> statesId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        SearchTerms searchTerm = new SearchTerms(customer, term);

        searchTerm = repository.save(searchTerm);

        if(!statesId.isEmpty()){
            for (Long id : statesId) {
                State state = stateRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado"));
                searchTerm.addState(state);
            }
        }

        return searchTerm;
    }
}