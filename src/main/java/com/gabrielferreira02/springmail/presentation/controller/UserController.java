package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("{id}")
    public User getUserById(@PathVariable UUID id) {
        return null;
    }

    @PutMapping("{id}")
    public void updateUser(@PathVariable UUID id, UpdateUserDTO body) {

    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable UUID id) {

    }
}
