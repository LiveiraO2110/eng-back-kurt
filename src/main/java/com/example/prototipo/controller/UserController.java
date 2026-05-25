package com.example.prototipo.controller;

import com.example.prototipo.records.requests.UserRequest;
import com.example.prototipo.records.response.UserDTO;
import com.example.prototipo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(service.createUser(request.name())));
    }
}
