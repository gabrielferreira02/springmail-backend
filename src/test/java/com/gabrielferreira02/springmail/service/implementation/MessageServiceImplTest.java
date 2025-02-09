package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.MessageRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.CreateMessageDTO;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessageServiceImpl messageService;

    @Nested
    class ResponseMessage {
        @Test
        @DisplayName("It should send a new message successfully")
        void success() {
            User user = new User(null, "user", "user@springmail.com", "12345678");
            Chat chat = new Chat(UUID.randomUUID(), "Subject", user, user, false, List.of(), Instant.now(), Instant.now());

            CreateMessageDTO body = new CreateMessageDTO(String.valueOf(chat.getId()), user.getEmail(), "content");

            when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
            when(chatRepository.findById(chat.getId())).thenReturn(Optional.of(chat));

            ResponseEntity<Map<String, String>> response = messageService.responseMessage(body);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Message send with success", response.getBody().get("message"));
        }

        @Test
        @DisplayName("It should fail on send a new message because user doesn't exists")
        void errorCase1() {
            UUID id = UUID.randomUUID();

            CreateMessageDTO body = new CreateMessageDTO(String.valueOf(id), "error@springmail.com", "content");

            when(userRepository.findByEmail("error@springmail.com")).thenReturn(null);

            ResponseEntity<Map<String, String>> response = messageService.responseMessage(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("User not found", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on send a new message because content is empty")
        void errorCase2() {
            UUID id = UUID.randomUUID();

            CreateMessageDTO body = new CreateMessageDTO(String.valueOf(id), "user@springmail.com", "");

            ResponseEntity<Map<String, String>> response = messageService.responseMessage(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Content field cannot be empty", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on send a new message because chat doesn't exists")
        void errorCase3() {
            UUID id = UUID.randomUUID();

            CreateMessageDTO body = new CreateMessageDTO(String.valueOf(id), "user@springmail.com", "content");

            when(userRepository.findByEmail("user@springmail.com")).thenReturn(new User());
            when(chatRepository.findById(id)).thenReturn(Optional.empty());

            ResponseEntity<Map<String, String>> response = messageService.responseMessage(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Chat id not found", response.getBody().get("error"));
        }
    }

    @Nested
    class GetAllMessagesByChatId {
        @Test
        @DisplayName("It should return all messages by chat id")
        void success() {
            MessageDTO messageDto = new MessageDTO("user", "user@springmail.com", null, "content");

            when(messageRepository.findAllMessagesByChatId(any(UUID.class))).thenReturn(List.of(messageDto));

            List<MessageDTO> response = messageService.getAllMessagesByChatId(UUID.randomUUID());

            assertNotNull(response);
            assertEquals(1, response.size());
            assertEquals("user", response.getFirst().senderName());
            assertEquals("user@springmail.com", response.getFirst().senderMail());
            assertEquals("content", response.getFirst().content());
        }
    }

}