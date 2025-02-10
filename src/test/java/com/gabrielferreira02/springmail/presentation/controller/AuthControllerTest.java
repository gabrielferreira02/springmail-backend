package com.gabrielferreira02.springmail.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import com.gabrielferreira02.springmail.presentation.dto.LoginRequestDTO;
import com.gabrielferreira02.springmail.service.implementation.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AuthServiceImpl authService;

    @Test
    @DisplayName("It should login a user successfully")
    void login() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@springmail.com", "12345678");

        String body = objectMapper.writeValueAsString(loginRequest);

        Map<String, String> map = new HashMap<>();
        map.put("email", "user@springmail.com");
        map.put("token", "token");
        ResponseEntity<Map<String, String>> response = ResponseEntity.ok(map);

        when(authService.login(loginRequest)).thenReturn(response);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(loginRequest.email()))
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @DisplayName("It should fail on login a user because email is empty")
    void loginError() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("", "12345678");

        String body = objectMapper.writeValueAsString(loginRequest);

        when(authService.login(loginRequest)).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on login a user because password is empty")
    void loginError1() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("user@springmail.com", "");

        String body = objectMapper.writeValueAsString(loginRequest);

        when(authService.login(loginRequest)).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should create a user with success")
    void register() throws Exception {
        CreateUserDTO user = new CreateUserDTO("user", "12345678", "user");

        String body = objectMapper.writeValueAsString(user);

        Map<String, String> message = new HashMap<>();
        message.put("message", "User created.");
        when(authService.register(user)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(message));

        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(jsonPath("$.message").value("User created."))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("It should fail on create a user because user is empty")
    void registerError1() throws Exception {
        CreateUserDTO user = new CreateUserDTO("", "12345678", "user");

        String body = objectMapper.writeValueAsString(user);

        Map<String, String> message = new HashMap<>();
        message.put("error", "Field username cannot be empty");
        when(authService.register(user)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(jsonPath("$.error").value( "Field username cannot be empty"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should fail on create a user because password is less than 8 characters")
    void registerError2() throws Exception {
        CreateUserDTO user = new CreateUserDTO("user", "1234", "user");

        String body = objectMapper.writeValueAsString(user);

        Map<String, String> message = new HashMap<>();
        message.put("error", "Field password cannot be empty or less than 8 characters");
        when(authService.register(user)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(jsonPath("$.error").value( "Field password cannot be empty or less than 8 characters"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should fail on create a user because email is empty")
    void registerError3() throws Exception {
        CreateUserDTO user = new CreateUserDTO("user", "12345678", "");

        String body = objectMapper.writeValueAsString(user);

        Map<String, String> message = new HashMap<>();
        message.put("error", "Field email cannot be empty");
        when(authService.register(user)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(jsonPath("$.error").value( "Field email cannot be empty"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should fail on create a user because user email already exists")
    void registerError4() throws Exception {
        CreateUserDTO user = new CreateUserDTO("user", "12345678", "user");

        String body = objectMapper.writeValueAsString(user);

        Map<String, String> message = new HashMap<>();
        message.put("error", "Email already exists. Try other.");
        when(authService.register(user)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(jsonPath("$.error").value( "Email already exists. Try other."))
                .andExpect(status().isBadRequest());
    }
}