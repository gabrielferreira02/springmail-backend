package com.gabrielferreira02.springmail.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.ChatInfoDTO;
import com.gabrielferreira02.springmail.presentation.dto.CreateChatDTO;
import com.gabrielferreira02.springmail.service.implementation.ChatServiceImpl;
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
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ChatServiceImpl chatService;

    @Test
    @DisplayName("It should return chat by user id")
    @WithMockUser
    void getChatsByUserId() throws Exception {
        String email = "user@springmail.com";

        ChatDTO chat = new ChatDTO(null, "subject", "username", "message", false, null, false);

        when(chatService.getChatsByUserEmail(email)).thenReturn(List.of(chat));

        mockMvc.perform(get("/chat/user/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("It should block returning chat by user id")
    void getChatsByUserIdError() throws Exception {
        String email = "user@springmail.com";

        mockMvc.perform(get("/chat/user/" + email))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should return sent chats by user id")
    @WithMockUser(roles = "USER")
    void getSentChatsByUserId() throws Exception {
        String email = "user@springmail.com";

        ChatDTO chat = new ChatDTO(null, "subject", "username", "message", false, null, false);

        when(chatService.getSentChatsByUserEmail(email)).thenReturn(List.of(chat));

        mockMvc.perform(get("/chat/user/sent/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("It should block returning sent chats by user id")
    void getSentChatsByUserIdError() throws Exception {
        String email = "user@springmail.com";

        mockMvc.perform(get("/chat/user/sent/" + email))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should return sent chats filtered by username")
    @WithMockUser
    void getSentChatsByUsername() throws Exception {
        String from = "user@springmail.com";
        String to = "user";

        ChatDTO chat = new ChatDTO(null, "subject", "username", "message", false, null, false);

        when(chatService.getSentChatsByUsername(from, to)).thenReturn(List.of(chat));

        mockMvc.perform(get("/chat/user/to?from=" + from + "&to=" + to))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("It should block returning sent chats filtered by username")
    void getSentChatsByUsernameError() throws Exception {
        String from = "user@springmail.com";
        String to = "user";
        mockMvc.perform(get("/chat/user/to?from=" + from + "&to="+ to ))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should return a list of received chats filtered by username")
    @WithMockUser
    void getReceivedChatsByUsername() throws Exception {
        String to = "user@springmail.com";
        String from = "user";

        ChatDTO chat = new ChatDTO(null, "subject", "username", "message", false, null, false);

        when(chatService.getReceivedChatsByUsername(from, to)).thenReturn(List.of(chat));

        mockMvc.perform(get("/chat/user/from?from=" + from + "&to=" + to))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("It should return a list of received chats filtered by username")
    @WithMockUser
    void getReceivedChatsByUsernameError() throws Exception {
        String to = "user@springmail.com";
        String from = "user";

        ChatDTO chat = new ChatDTO(null, "subject", "username", "message", false, null, false);

        when(chatService.getReceivedChatsByUsername(from, to)).thenReturn(List.of(chat));

        mockMvc.perform(get("/chat/user/from?from=" + from + "&to=" + to))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("It should return chat information with success")
    @WithMockUser
    void getChatById() throws Exception {
        UUID id = UUID.randomUUID();

        ChatInfoDTO chat = new ChatInfoDTO(id, "subject", "user@springmail.com", "user2@springmail.com");

        when(chatService.getByChatId(id)).thenReturn(chat);

        mockMvc.perform(get("/chat/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("subject"))
                .andExpect(jsonPath("$.fromEmail").value("user@springmail.com"))
                .andExpect(jsonPath("$.toEmail").value("user2@springmail.com"));
    }

    @Test
    @DisplayName("It should block returning chat information")
    void getChatByIdError1() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/chat/" + id))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should return chat information with success")
    @WithMockUser
    void getChatByIdError2() throws Exception {
        UUID id = UUID.randomUUID();

        when(chatService.getByChatId(id)).thenReturn(null);

        mockMvc.perform(get("/chat/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("It should create a new chat with success")
    @WithMockUser
    void createChat() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "subject",
                "user2@springmail.com",
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> message = new HashMap<>();
        message.put("message", "Mensagem enviada");

        when(chatService.createChat(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(message));

        mockMvc.perform(post("/chat")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Mensagem enviada"));
    }

    @Test
    @DisplayName("It should block creating a new chat")
    void createChatError1() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "subject",
                "user2@springmail.com",
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);


        mockMvc.perform(post("/chat")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on create a new chat because subject is empty")
    @WithMockUser
    void createChatError2() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "",
                "user2@springmail.com",
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> message = new HashMap<>();
        message.put("error", "Campo assunto não pode estar vazio");

        when(chatService.createChat(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/chat")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value( "Campo assunto não pode estar vazio"));
    }

    @Test
    @DisplayName("It should fail on create a new chat because destination is empty")
    @WithMockUser
    void createChatError3() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "subject",
                "",
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> message = new HashMap<>();
        message.put("error", "Campo do destinatário não pode estar vazio");

        when(chatService.createChat(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/chat")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value( "Campo do destinatário não pode estar vazio"));
    }

    @Test
    @DisplayName("It should fail on create a new chat because sender is empty")
    @WithMockUser
    void createChatError4() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "subject",
                "user@springmail.com",
                "",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> message = new HashMap<>();
        message.put("error", "Falha ao localizar seu email");

        when(chatService.createChat(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/chat")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value( "Falha ao localizar seu email"));
    }

    @Test
    @DisplayName("It should fail on create a new chat because content is empty")
    @WithMockUser
    void createChatError5() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "subject",
                "user2@springmail.com",
                "user@springmail.com",
                ""
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> message = new HashMap<>();
        message.put("error", "Mensagem não pode ser vazia");

        when(chatService.createChat(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/chat")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value( "Mensagem não pode ser vazia"));
    }

    @Test
    @DisplayName("It should fail on create a new chat because user destination not found")
    @WithMockUser
    void createChatError6() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "subject",
                "error@springmail.com",
                "user@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> message = new HashMap<>();
        message.put("error", "Email não encontrado");

        when(chatService.createChat(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/chat")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value( "Email não encontrado"));
    }

    @Test
    @DisplayName("It should fail on create a new chat because user sender not found")
    @WithMockUser
    void createChatError7() throws Exception {
        CreateChatDTO requestBody = new CreateChatDTO(
                "subject",
                "user2@springmail.com",
                "error@springmail.com",
                "content"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> message = new HashMap<>();
        message.put("error", "Falha ao localizar seu email");

        when(chatService.createChat(requestBody)).thenReturn(ResponseEntity.badRequest().body(message));

        mockMvc.perform(post("/chat")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value( "Falha ao localizar seu email"));
    }
}