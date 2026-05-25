package com.example.prototipo.service;

import com.example.prototipo.models.User;
import com.example.prototipo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public User createUser(String name){
        return repository.save(new User(name));
    }
}
