package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import com.gabrielferreira02.springmail.presentation.dto.LoginRequestDTO;
import com.gabrielferreira02.springmail.service.interfaces.AuthService;
import com.gabrielferreira02.springmail.utility.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
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
                passwordEncoder.encode(body.password())
        );

        System.out.println(userRepository.save(newUser).getId());
        message.put("message", "User created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Override
    public ResponseEntity<?> login(LoginRequestDTO body) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.email(),
                        body.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication.getName());

        Map<String, String> response = new HashMap<>();
        response.put("email", authentication.getName());
        response.put("token", jwt);
        return ResponseEntity.ok(response);
    }
}
