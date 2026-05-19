package com.example.prototipo.controller;

import com.example.prototipo.records.requests.ProcurementRequest;
import com.example.prototipo.records.response.ProcurementDTO;
import com.example.prototipo.service.ProcurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procurement")
public class ControllerProcurement {
    @Autowired
    private ProcurementService service;

    @GetMapping
    public List<ProcurementDTO> getAll(){
        return service.getAll().stream().map(ProcurementDTO::new).toList();
    }

    @GetMapping("/customer/{id}")
    public List<ProcurementDTO> getAllByCustomer(@PathVariable("id") Long id){
        return service.getAllByCustomer(id).stream().map(ProcurementDTO::new).toList();
    }

//    @GetMapping("/search")
//    public List<ProcurementDTO> filter (
//            @RequestParam(value = "data"),
//            @
//    )

    @PostMapping
    public ResponseEntity<ProcurementDTO> createProcurement(@RequestBody ProcurementRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProcurementDTO(service.createProcurement(request)));
    }
}