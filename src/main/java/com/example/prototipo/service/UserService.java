package com.example.prototipo.service;

import com.example.prototipo.models.User;
import com.example.prototipo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User createUser(String name){
        return repository.save(new User(name));
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public User getById(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }
}
