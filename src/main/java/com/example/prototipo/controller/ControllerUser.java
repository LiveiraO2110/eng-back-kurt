package com.example.prototipo.controller;

import com.example.prototipo.records.requests.UserRequest;
import com.example.prototipo.records.response.UserDTO;
import com.example.prototipo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ControllerUser {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(service.createUser(request.name())));
    }

    @GetMapping
    public List<UserDTO> getAll(){
        return service.getAll().stream().map(UserDTO::new).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id){
        return ResponseEntity.ok(new UserDTO(service.getById(id)));
    }
}