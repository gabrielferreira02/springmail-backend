package com.gabrielferreira02.springmail.service.interfaces;

import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    ResponseEntity<Map<String, String>> register(CreateUserDTO body);
    ResponseEntity<?> login();
}
