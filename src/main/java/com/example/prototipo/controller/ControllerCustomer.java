package com.example.prototipo.controller;

import com.example.prototipo.records.requests.CustomerRequest;
import com.example.prototipo.records.requests.SearchTermBody;
import com.example.prototipo.records.requests.SearchTermsRequest;
import com.example.prototipo.records.response.CustomerDTO;
import com.example.prototipo.records.response.ProcurementDTO;
import com.example.prototipo.records.response.SearchTermDTO;
import com.example.prototipo.service.CustomerService;
import com.example.prototipo.service.SearchTermsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class ControllerCustomer {
    @Autowired
    private CustomerService service;
    @Autowired
    private SearchTermsService termsService;

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

    @GetMapping("/search-terms")
    public List<SearchTermDTO> getAllTerms(){
        return termsService.getAll().stream().map(SearchTermDTO::new).toList();
    }

    @GetMapping("/search-terms/{id}")
    public ResponseEntity<SearchTermDTO> getTermById(@PathVariable("id") Long id){
        return ResponseEntity.ok(new SearchTermDTO(termsService.getById(id)));
    }

    @PostMapping("/{id}/search-terms")
    public ResponseEntity<Set<SearchTermDTO>> createTerm(@Valid @RequestBody SearchTermsRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(termsService.create(request.customerId(), request.terms())
                .stream().map(SearchTermDTO::new).collect(Collectors.toSet()));
    }

    @DeleteMapping("/{customerId}/search-terms/{termId}")
    public ResponseEntity<Set<SearchTermDTO>> createTerm(@PathVariable("customerId") Long customerId, @PathVariable("termId") Long termId){
        return ResponseEntity.ok(termsService.deleteById(customerId, termId).stream().map(SearchTermDTO::new).collect(Collectors.toSet()));
    }
}