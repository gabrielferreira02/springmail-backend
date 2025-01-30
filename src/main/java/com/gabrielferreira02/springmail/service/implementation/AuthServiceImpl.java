package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import com.gabrielferreira02.springmail.service.interfaces.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Map<String, String>> register(CreateUserDTO body) {
        Map<String, String> message = new HashMap<>();

        if(body.username().isEmpty()) {
            message.put("error", "Field username cannot be empty");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.password().isEmpty() ||
                body.password().length() < 8) {
            message.put("error", "Field password cannot be empty");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.email().isEmpty()) {
            message.put("error", "Field email cannot be empty");
            return ResponseEntity.badRequest().body(message);
        }

        if(userRepository.findByEmail(body.email() + "@springmail.com") != null) {
            message.put("error", "Email already exists. Try other.");
            return ResponseEntity.badRequest().body(message);
        }

        User newUser = new User(
                null,
                body.username(),
                body.email() + "@springmail.com",
                body.password()
        );

        System.out.println(userRepository.save(newUser).getId());
        message.put("message", "User created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Override
    public ResponseEntity<?> login() {
        return null;
    }
}
