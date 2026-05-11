package com.example.prototipo.service;

import com.example.prototipo.models.Customer;
import com.example.prototipo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    public List<Customer> getAll(){
        return repository.findAll();
    }
}
