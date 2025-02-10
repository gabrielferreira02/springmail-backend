package com.gabrielferreira02.springmail.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Favorite;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.FavoriteDTO;
import com.gabrielferreira02.springmail.service.implementation.FavoriteServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private FavoriteServiceImpl favoriteService;

    @Test
    @DisplayName("It should return favorite chats of a user")
    @WithMockUser(roles = "USER")
    void findFavorites() throws Exception {
        ChatDTO chat = new ChatDTO(null, "subject", "user", "message", false, null, true);
        String email = "user@springmail.com";

        when(favoriteService.findFavorites(email)).thenReturn(List.of(chat));

        mockMvc.perform(get("/favorite/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("It should block returning favorite chats of a user")
    void findFavoritesError() throws Exception {
       String email = "user@springmail.com";

        mockMvc.perform(get("/favorite/" + email))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should return a list of favorite chats filtered by username")
    @WithMockUser(roles = "USER")
    void findFavoriteChatsByUsername() throws Exception {
        ChatDTO chat = new ChatDTO(null, "subject", "user", "message", false, null, true);

        when(favoriteService.findFavoriteChatsByUsername("user","user@springmail.com")).thenReturn(List.of(chat));

        mockMvc.perform(get("/favorite?username=user&userEmail=user@springmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @DisplayName("It should block returning a list of favorite chats filtered by username")
    void findFavoriteChatsByUsernameError() throws Exception {
        mockMvc.perform(get("/favorite?username=user&userEmail=user@springmail.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should favorite a chat with success")
    @WithMockUser(roles = "USER")
    void favorite() throws Exception {
        FavoriteDTO requestBody = new FavoriteDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com"
        );

        Favorite favorite = new Favorite(
                null,
                new Chat(UUID.fromString(requestBody.chatId()), "", null, null, false, null, null, null),
                new User(UUID.randomUUID(),"",requestBody.userEmail(),"")
        );

        String body = objectMapper.writeValueAsString(requestBody);

        when(favoriteService.favorite(requestBody)).thenReturn(ResponseEntity.ok(favorite));

        mockMvc.perform(post("/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chat.id").value(requestBody.chatId()));
    }

    @Test
    @DisplayName("It should block favorite a chat")
    void favoriteError1() throws Exception {
        FavoriteDTO requestBody = new FavoriteDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on favorite a chat because chat id is empty")
    @WithMockUser(roles = "USER")
    void favoriteError2() throws Exception {
        FavoriteDTO requestBody = new FavoriteDTO(
                "",
                "user@springmail.com"
        );


        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Campo chatId não pode estar vazio");

        when(favoriteService.favorite(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campo chatId não pode estar vazio"));
    }

    @Test
    @DisplayName("It should fail on favorite a chat because chat id is empty")
    @WithMockUser(roles = "USER")
    void favoriteError3() throws Exception {
        FavoriteDTO requestBody = new FavoriteDTO(
                String.valueOf(UUID.randomUUID()),
                ""
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Campo email de usuario não pode estar vazio");

        when(favoriteService.favorite(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campo email de usuario não pode estar vazio"));
    }

    @Test
    @DisplayName("It should fail on favorite a chat because chat not found")
    @WithMockUser(roles = "USER")
    void favoriteError4() throws Exception {
        FavoriteDTO requestBody = new FavoriteDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Chat id não existe");

        when(favoriteService.favorite(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Chat id não existe"));
    }

    @Test
    @DisplayName("It should fail on favorite a chat because user not found")
    @WithMockUser(roles = "USER")
    void favoriteError5() throws Exception {
        FavoriteDTO requestBody = new FavoriteDTO(
                String.valueOf(UUID.randomUUID()),
                "error@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Campo email de usuario não pode estar vazio");

        when(favoriteService.favorite(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campo email de usuario não pode estar vazio"));
    }

    @Test
    @DisplayName("It should fail on favorite a chat because chat is already saved")
    @WithMockUser(roles = "USER")
    void favoriteError6() throws Exception {
        FavoriteDTO requestBody = new FavoriteDTO(
                String.valueOf(UUID.randomUUID()),
                "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Esse chat já está em seus favoritos");

        when(favoriteService.favorite(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(post("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Esse chat já está em seus favoritos"));
    }

    @Test
    @DisplayName("It should remove a chat from favorites with success")
    @WithMockUser(roles = "USER")
    void removeFavorite() throws Exception {

        when(favoriteService.removeFavorite(eq("user@springmail.com"), any(UUID.class))).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/favorite?userEmail=user@springmail.com&chatId=" + UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("It should block removing a chat from favorites")
    void removeFavoriteError1() throws Exception {

        when(favoriteService.removeFavorite(eq("user@springmail.com"), any(UUID.class))).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/favorite?userEmail=user@springmail.com&chatId=" + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on remove a chat from favorites because chat id is empty")
    @WithMockUser(roles = "USER")
    void removeFavoriteError2() throws Exception {

        mockMvc.perform(delete("/favorite?userEmail=user@springmail.com&chatId="))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should fail on remove a chat from favorites because user email is empty")
    @WithMockUser(roles = "USER")
    void removeFavoriteError3() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Campo email do ususario não pode estar vazio");

        when(favoriteService.removeFavorite(eq(""), any(UUID.class))).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(delete("/favorite?userEmail=&chatId=" + UUID.randomUUID()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Campo email do ususario não pode estar vazio"));
    }

    @Test
    @DisplayName("It should fail on remove a chat from favorites because user not found")
    @WithMockUser(roles = "USER")
    void removeFavoriteError4() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario não encontrado");

        when(favoriteService.removeFavorite(eq("error@springmail.com"), any(UUID.class))).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(delete("/favorite?userEmail=error@springmail.com&chatId=" + UUID.randomUUID()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Usuario não encontrado"));
    }

    @Test
    @DisplayName("It should fail on remove a chat from favorites because favorite chat not found")
    @WithMockUser(roles = "USER")
    void removeFavoriteError5() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Esse chat não está em seus favoritos");

        when(favoriteService.removeFavorite(eq("error@springmail.com"), any(UUID.class))).thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response));

        mockMvc.perform(delete("/favorite?userEmail=error@springmail.com&chatId=" + UUID.randomUUID()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Esse chat não está em seus favoritos"));
    }
}