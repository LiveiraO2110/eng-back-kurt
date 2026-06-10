package com.example.prototipo.service;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import com.example.prototipo.models.SearchTerms;
import com.example.prototipo.models.User;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.ProcurementRepository;
import com.example.prototipo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private ProcurementRepository procurementRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Customer> getAll(){
        return repository.findAll();
    }

    public List<Procurement> getProcurement(Long customerId){
        return procurementRepository.findByCustomer_Id(customerId);
    }

    public Customer createCustomer(Long userId, String name ){
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return repository.save(new Customer(user, name));
    }

    public Set<SearchTerms> getSearchTerms(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"))
                .getSearchTerms();
    }
}
