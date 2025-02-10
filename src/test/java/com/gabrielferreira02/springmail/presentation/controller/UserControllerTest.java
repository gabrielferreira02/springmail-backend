package com.gabrielferreira02.springmail.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.UpdatePasswordDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUsernameDTO;
import com.gabrielferreira02.springmail.service.implementation.UserServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserServiceImpl userService;

    @Test
    @DisplayName("It should return a user by email with success")
    @WithMockUser(roles = "USER")
    void getUserByEmail() throws Exception {
        User user = new User(
                null,
                "user",
                "user@springmail.com",
                "12345678"
        );

        when(userService.getUserByEmail("user@springmail.com")).thenReturn(user);

        mockMvc.perform(get("/user/user@springmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

    }

    @Test
    @DisplayName("It should block return a user by email ")
    void getUserByEmailError() throws Exception {

        mockMvc.perform(get("/user/user@springmail.com"))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("It should update username with success")
    @WithMockUser(roles = "USER")
    void updateUsername() throws Exception {
        UpdateUsernameDTO requestBody = new UpdateUsernameDTO(
                "user", "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        when(userService.updateUsername(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        mockMvc.perform(put("/user/username")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("It should block in update the username")
    void updateUsernameError1() throws Exception {
        UpdateUsernameDTO requestBody = new UpdateUsernameDTO(
                "user", "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(put("/user/username")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("It should fail in update a user because username is empty")
    @WithMockUser(roles = "USER")
    void updateUsernameError2() throws Exception {
        UpdateUsernameDTO requestBody = new UpdateUsernameDTO(
                "", "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        when(userService.updateUsername(requestBody)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(put("/user/username")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("It should fail in update a user not found")
    @WithMockUser(roles = "USER")
    void updateUsernameError3() throws Exception {
        UpdateUsernameDTO requestBody = new UpdateUsernameDTO(
                "", "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        when(userService.updateUsername(requestBody)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put("/user/username")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("It should update password with success")
    @WithMockUser(roles = "USER")
    void updatePassword() throws Exception {
        UpdatePasswordDTO requestBody = new UpdatePasswordDTO(
                "123456789",
                "12345678",
                "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        when(userService.updatePassword(requestBody)).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        mockMvc.perform(put("/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("It should block update password")
    void updatePasswordError1() throws Exception {
        UpdatePasswordDTO requestBody = new UpdatePasswordDTO(
                "123456789",
                "12345678",
                "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("It should fail on update password because newPassword has length less than 8")
    @WithMockUser(roles = "USER")
    void updatePasswordError2() throws Exception {
        UpdatePasswordDTO requestBody = new UpdatePasswordDTO(
                "1234567",
                "12345678",
                "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        when(userService.updatePassword(requestBody)).thenReturn(ResponseEntity.badRequest().build());

        mockMvc.perform(put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("It should fail on update password because user not found")
    @WithMockUser(roles = "USER")
    void updatePasswordError3() throws Exception {
        UpdatePasswordDTO requestBody = new UpdatePasswordDTO(
                "123456789",
                "1234567",
                "user@springmail.com"
        );

        String body = objectMapper.writeValueAsString(requestBody);

        when(userService.updatePassword(requestBody)).thenReturn(ResponseEntity.badRequest().body("Senha incorreta"));

        mockMvc.perform(put("/user/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Senha incorreta"));
    }

    @Test
    @DisplayName("It should delete a user with success")
    @WithMockUser(roles = "USER")
    void deleteUser() throws Exception {

        when(userService.deleteUser("user@springmail.com")).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/user/user@springmail.com"))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("It should block on delete a user")
    void deleteUserError1() throws Exception {

        mockMvc.perform(delete("/user/user@springmail.com"))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("It should delete a user with success")
    @WithMockUser(roles = "USER")
    void deleteUserError2() throws Exception {

        when(userService.deleteUser("error@springmail.com")).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/user/error@springmail.com"))
                .andExpect(status().isNotFound());

    }
}