package com.gabrielferreira02.springmail.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.springmail.presentation.dto.CreateMessageDTO;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import com.gabrielferreira02.springmail.service.implementation.MessageServiceImpl;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private MessageServiceImpl messageService;

    @Test
    @DisplayName("It should create a new message in a chat with success")
    @WithMockUser(roles = "USER")
    void responseMessage() throws Exception {
        CreateMessageDTO requestBody = new CreateMessageDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        Map<String, String> message = new HashMap<>();
        message.put("message", "Sent message with success");
        when(messageService.responseMessage(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(message));

        mockMvc.perform(post("/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Sent message with success"));

    }

    @Test
    @DisplayName("It should block on create a new message in a chat")
    void responseMessageError1() throws Exception {
        CreateMessageDTO requestBody = new CreateMessageDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on create a new message in a chat because content is empty")
    @WithMockUser(roles = "USER")
    void responseMessageError2() throws Exception {
        CreateMessageDTO requestBody = new CreateMessageDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com",
                ""
        );

        String body = objectMapper.writeValueAsString(requestBody);

        Map<String, String> message = new HashMap<>();
        message.put("error", "Content field cannot be empty");
        when(messageService.responseMessage(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Content field cannot be empty"));
    }

    @Test
    @DisplayName("It should fail on create a new message in a chat because user not found")
    @WithMockUser(roles = "USER")
    void responseMessageError3() throws Exception {
        CreateMessageDTO requestBody = new CreateMessageDTO(
                String.valueOf(UUID.randomUUID()),
                "error@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        Map<String, String> message = new HashMap<>();
        message.put("error", "User not found");
        when(messageService.responseMessage(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    @DisplayName("It should fail on create a new message in a chat because chat not found")
    @WithMockUser(roles = "USER")
    void responseMessageError4() throws Exception {
        CreateMessageDTO requestBody = new CreateMessageDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        Map<String, String> message = new HashMap<>();
        message.put("error", "Chat id not found");
        when(messageService.responseMessage(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Chat id not found"));
    }

    @Test
    @DisplayName("It should return a list of messages by chat id with success")
    @WithMockUser(roles = "USER")
    void getAllMessagesByChatId() throws Exception {
        UUID id = UUID.randomUUID();

        when(messageService.getAllMessagesByChatId(id)).thenReturn(List.of(new MessageDTO("user", "user@springmail.com", Instant.now(), "content")));

        mockMvc.perform(get("/message/chat/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));

    }

    @Test
    @DisplayName("It should block returning a list of messages by chat id")
    void getAllMessagesByChatIdError() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/message/chat/" + id))
                .andExpect(status().isForbidden());

    }
}