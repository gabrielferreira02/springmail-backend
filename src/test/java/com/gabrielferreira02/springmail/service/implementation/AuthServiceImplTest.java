package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import com.gabrielferreira02.springmail.presentation.dto.LoginRequestDTO;
import com.gabrielferreira02.springmail.utility.utils.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    class Register {

        @Test
        @DisplayName("It should create a new user with success")
        void success() {
            CreateUserDTO user = new CreateUserDTO("user", "12345678", "user");

            when(userRepository.findByEmail(eq("user" + "@springmail.com"))).thenReturn(null);

            ResponseEntity<Map<String, String>> response = authService.register(user);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("User created.", response.getBody().get("message"));
        }

        @Test
        @DisplayName("It should fail on create a user because username is empty")
        void errorCase1() {
            CreateUserDTO user = new CreateUserDTO("", "12345678", "user");

            ResponseEntity<Map<String, String>> response = authService.register(user);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Field username cannot be empty", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a user because password is less than 8")
        void errorCase2() {
            CreateUserDTO user = new CreateUserDTO("user", "1234567", "user");

            ResponseEntity<Map<String, String>> response = authService.register(user);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Field password cannot be empty or less than 8 characters", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a user because email is empty")
        void errorCase3() {
            CreateUserDTO user = new CreateUserDTO("user", "12345678", "user");

            when(userRepository.findByEmail(eq("user" + "@springmail.com"))).thenReturn(new User());

            ResponseEntity<Map<String, String>> response = authService.register(user);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Email already exists. Try other.", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a new user because email is not valid")
        void errorCase4() {
            CreateUserDTO user = new CreateUserDTO("user", "12345678", "user@123");

            ResponseEntity<Map<String, String>> response = authService.register(user);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Email tem que ter apenas letras e numeros", response.getBody().get("error"));
        }

    }

    @Nested
    class Login {
        @Test
        @DisplayName("It should login a user with success")
        void success() {
            LoginRequestDTO body = new LoginRequestDTO(
                    "user@springmail.com",
                    "12345678"
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(body.email(), body.password());

            when(authenticationManager.authenticate(any())).thenReturn(auth);
            when(jwtUtil.generateToken(any())).thenReturn("token");

            ResponseEntity<Map<String, String>> response = authService.login(body);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("user@springmail.com", response.getBody().get("email"));
            assertEquals("token", response.getBody().get("token"));
        }

        @Test
        @DisplayName("It should fail on login a user because")
        void error() {
            LoginRequestDTO body = new LoginRequestDTO(
                    "user@springmail.com",
                    "12345678"
            );

            when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

            assertThrows(BadCredentialsException.class, () -> {
                authService.login(body);
            });
        }
    }
}