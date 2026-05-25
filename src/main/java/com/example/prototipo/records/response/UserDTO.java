package com.example.prototipo.records.response;

import com.example.prototipo.models.User;

public record UserDTO (
        Long id,
        String name
){
    public UserDTO(User user){
        this(
                user.getId(),
                user.getName()
        );
    }
}
