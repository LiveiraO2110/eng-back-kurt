package com.example.prototipo.controller;

import com.example.prototipo.records.requests.CustomerRequest;
import com.example.prototipo.records.response.CustomerDTO;
import com.example.prototipo.records.response.ProcurementDTO;
import com.example.prototipo.records.response.SearchTermDTO;
import com.example.prototipo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class ControllerCustomer {
    @Autowired
    private CustomerService service;

    @GetMapping
    public List<CustomerDTO> getAll(){
        return service.getAll().stream().map(CustomerDTO::new).toList();
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerRequest customer){
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomerDTO(service.createCustomer(customer.userId(), customer.name())));
    }

    @GetMapping("/{id}/search-terms")
    public List<SearchTermDTO> getSearchTermsByCustomer(@PathVariable("id") Long id){
        return service.getSearchTerms(id)
                .stream().map(SearchTermDTO::new)
                .toList();
    }

    @GetMapping("/{id}/procurements")
    public List<ProcurementDTO> getAllProcurementByCustomer(@PathVariable("id") Long id){
        return service.getProcurement(id).stream().map(ProcurementDTO::new).toList();
    }
}
