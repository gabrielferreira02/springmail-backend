package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import com.gabrielferreira02.springmail.presentation.dto.LoginRequestDTO;
import com.gabrielferreira02.springmail.service.implementation.AuthServiceImpl;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PreAuthorize("permitAll()")
    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO body) {
        return authService.login(body);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("register")
    public ResponseEntity<Map<String, String>> register(@RequestBody CreateUserDTO body) {
        return authService.register(body);
    }

}
