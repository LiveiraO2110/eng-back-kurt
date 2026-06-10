package com.example.prototipo.controller;

import com.example.prototipo.records.requests.SearchTermRequest;
import com.example.prototipo.records.response.SearchTermDTO;
import com.example.prototipo.service.SearchTermsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers/search-terms")
public class ControllerSearchTerms {
    @Autowired
    private SearchTermsService service;

    @GetMapping
    public List<SearchTermDTO> getAll(){
        return service.getAll().stream().map(SearchTermDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchTermDTO> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(new SearchTermDTO(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<SearchTermDTO> create(@Valid @RequestBody SearchTermRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new SearchTermDTO(service.create(request.customerId(), request.term(), request.statesId())));
    }
}