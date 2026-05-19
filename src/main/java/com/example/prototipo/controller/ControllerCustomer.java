package com.example.prototipo.controller;

import com.example.prototipo.records.requests.CustomerRequest;
import com.example.prototipo.records.requests.TermRequest;
import com.example.prototipo.records.response.CustomerDTO;
import com.example.prototipo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerRequest customer){
        return ResponseEntity.status(HttpStatus.CREATED).body(new CustomerDTO(service.createCustomer(customer)));
    }

    @PutMapping("/{id}/searchTerms/add")
    public ResponseEntity<CustomerDTO> addSearchTerms(@PathVariable Long id, @RequestBody Set<String> terms){
        return ResponseEntity.ok().body(new CustomerDTO(service.addSearchTerms(id, terms)));
    }

    @PutMapping("/{id}/searchTerms/remove")
    public ResponseEntity<CustomerDTO> removeSearchTerms(@PathVariable Long id, @RequestBody TermRequest term){
        return ResponseEntity.ok().body(new CustomerDTO(service.removeSearchTerm(id, term.name())));
    }
}
