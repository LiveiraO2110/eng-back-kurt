package com.example.prototipo.controller;

import com.example.prototipo.enums.Date;
import com.example.prototipo.records.requests.ProcurementRequest;
import com.example.prototipo.records.response.ProcurementDTO;
import com.example.prototipo.service.ProcurementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procurements")
public class ControllerProcurement {
    @Autowired
    private ProcurementService service;

    @GetMapping
    public List<ProcurementDTO> getAll(){
        return service.getAll().stream().map(ProcurementDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcurementDTO> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(new ProcurementDTO(service.getById(id)));
    }

    @GetMapping("/search")
    public List<ProcurementDTO> filter (
            @RequestParam(value = "data", required = false, defaultValue = "DIA") Date date,
            @RequestParam(value = "ufs", required = false, defaultValue = "RS") String ufs,
            @RequestParam(value = "cliente") Long customerId
    ){
        return service.search(customerId, date, ufs).stream().map(ProcurementDTO::new).toList();
    }
}