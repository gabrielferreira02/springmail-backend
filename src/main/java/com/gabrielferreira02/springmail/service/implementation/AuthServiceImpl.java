package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import com.gabrielferreira02.springmail.presentation.dto.LoginRequestDTO;
import com.gabrielferreira02.springmail.service.interfaces.AuthService;
import com.gabrielferreira02.springmail.utility.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
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
            log.error("Field username is empty");
            message.put("error", "Field username cannot be empty");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.password().isEmpty() ||
                body.password().length() < 8) {
            log.error("Field password is empty");
            message.put("error", "Field password cannot be empty or less than 8 characters");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.email().isEmpty()) {
            message.put("error", "Field email cannot be empty");
            return ResponseEntity.badRequest().body(message);
        }

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+$");
        Matcher matcher = pattern.matcher(body.email());
        if(!matcher.matches()) {
            log.error("Email {} is not valid", body.email());
            message.put("error", "Email tem que ter apenas letras e numeros");
            return ResponseEntity.badRequest().body(message);
        }

        if(userRepository.findByEmail(body.email() + "@springmail.com") != null) {
            log.error("Email {} already exists", body.email());
            message.put("error", "Email already exists. Try other.");
            return ResponseEntity.badRequest().body(message);
        }

        User newUser = new User(
                null,
                body.username(),
                body.email() + "@springmail.com",
                passwordEncoder.encode(body.password())
        );

        userRepository.save(newUser);
        log.info("User created with success");
        message.put("message", "User created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Override
    public ResponseEntity<Map<String, String>> login(LoginRequestDTO body) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.email(),
                        body.password()
                )
        );

        log.warn("Authenticating user");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication.getName());

        Map<String, String> response = new HashMap<>();
        response.put("email", authentication.getName());
        response.put("token", jwt);
        log.info("User authenticated whit success");
        return ResponseEntity.ok(response);
    }
}
