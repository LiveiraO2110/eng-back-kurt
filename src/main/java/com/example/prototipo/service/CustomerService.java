package com.example.prototipo.service;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.models.User;
import com.example.prototipo.records.requests.CustomerRequest;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.ProcurementRepository;
import com.example.prototipo.repository.SearchTermsRepository;
import com.example.prototipo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private ProcurementRepository procurementRepository;
    @Autowired
    private SearchTermsRepository searchTermsRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Customer> getAll(){
        return repository.findAll();
    }

    public List<Procurement> getProcurementByCustomer(Long customerId){
        return procurementRepository.findByCustomer_Id(customerId);
    }

    public Customer createCustomer(CustomerRequest request){
        User user = userRepository.findById(request.user_id()).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return repository.save(new Customer(user, request));
    }

    public Customer addSearchTerms(Long id, Set<String> terms){
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com id: "+id+", não encontrado"));

        for (String term : terms) {
            customer.addSearchTerm(searchTermsRepository.save(new SearchTerms(customer, term)));
        }

        return customer;
    }

    public Customer removeSearchTerm(Long id, String term){
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com id: "+id+", não encontrado"));

        searchTermsRepository.delete(
                searchTermsRepository.findByTermAndCustomer_Id(term, customer.getId())
                        .orElseThrow(() -> new EntityNotFoundException("O termo não está associado à este cliente"))
        );
        return customer;
    }
}
