package com.example.prototipo.service;

import com.example.prototipo.models.Customer;
import com.example.prototipo.models.Procurement;
import com.example.prototipo.records.requests.ProcurementRequest;
import com.example.prototipo.repository.CustomerRepository;
import com.example.prototipo.repository.ProcurementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcurementService {
    @Autowired
    private ProcurementRepository repository;
    @Autowired
    private CustomerRepository customerRepository;

    public List<Procurement> getAll(){
        return repository.findAll();
    }

    public List<Procurement> getAllByCustomer(Long customerId){
        return repository.findByCustomer_Id(customerId);
    }

    public Procurement createProcurement (ProcurementRequest request){
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente com id "+request.customerId()+" não encontrado"));

        return repository.save(new Procurement(customer, request.procurement()));
    }
}