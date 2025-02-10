package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.MessageRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.ChatInfoDTO;
import com.gabrielferreira02.springmail.presentation.dto.CreateChatDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatRepository chatRepository;
    @InjectMocks
    private ChatServiceImpl chatService;

    @Nested
    class getChatsByUserEmail {
        @Test
        @DisplayName("It should return chats by user email")
        void success() {
            String email = "user@springmail.com";

            User user = new User(UUID.randomUUID(), "user", email, "12345678");

            when(userRepository.findByEmail(eq(email))).thenReturn(user);
            when(chatRepository.getAllChatsByUserId(eq(user.getId()), any(Pageable.class))).thenReturn(List.of(new ChatDTO(null, "subject", "user", "message", false, null, false)));

            List<ChatDTO> response = chatService.getChatsByUserEmail(email);

            assertEquals(1, response.size());
            assertEquals("message", response.getFirst().message());
            assertEquals("subject", response.getFirst().subject());
        }

        @Test
        @DisplayName("It should return an empty list")
        void error() {
            String email = "error@springmail.com";

            when(userRepository.findByEmail(eq(email))).thenReturn(null);

            List<ChatDTO> response = chatService.getChatsByUserEmail(email);

            assertEquals(0, response.size());
        }
    }

    @Nested
    class getSentChatsByUserEmail {
        @Test
        @DisplayName("It should return sent chats by user email")
        void success() {
            String email = "user@springmail.com";

            User user = new User(UUID.randomUUID(), "user", email, "12345678");

            when(userRepository.findByEmail(eq(email))).thenReturn(user);
            when(chatRepository.getAllSentChatsByUserId(eq(user.getId()), any(Pageable.class))).thenReturn(List.of(new ChatDTO(null, "subject", "user", "message", false, null, false)));

            List<ChatDTO> response = chatService.getSentChatsByUserEmail(email);

            assertEquals(1, response.size());
            assertEquals("message", response.getFirst().message());
            assertEquals("subject", response.getFirst().subject());
        }

        @Test
        @DisplayName("It should return an empty list")
        void error() {
            String email = "error@springmail.com";

            when(userRepository.findByEmail(eq(email))).thenReturn(null);

            List<ChatDTO> response = chatService.getSentChatsByUserEmail(email);

            assertEquals(0, response.size());
        }
    }

    @Nested
    class getSentChatsByUsername {

        @Test
        @DisplayName("It should return a list of sent chats filtered by username")
        void success() {
            String from = "user@springmail.com";
            String to = "user 2";
            ChatDTO chat = new ChatDTO(
                    null,
                    "Subject",
                    "user",
                    "message",
                    true,
                    null,
                    false
            );

            when(chatRepository.getSentChatsByUsername(eq(from), eq(to), any(Pageable.class))).thenReturn(List.of(chat));

            List<ChatDTO> response = chatService.getSentChatsByUsername(from, to);

            assertEquals(1, response.size());
            assertEquals("user", response.getFirst().username());
            assertEquals("Subject", response.getFirst().subject());

        }
    }

    @Nested
    class getReceivedChatsByUsername {
        @Test
        @DisplayName("It should return a list of received chats filtered by username")
        void success() {
            String to = "user@springmail.com";
            String from = "user 2";
            ChatDTO chat = new ChatDTO(
                    null,
                    "Subject",
                    "user",
                    "message",
                    true,
                    null,
                    false
            );

            when(chatRepository.getReceivedChatsByUsername(eq(from), eq(to), any(Pageable.class))).thenReturn(List.of(chat));

            List<ChatDTO> response = chatService.getReceivedChatsByUsername(from, to);

            assertEquals(1, response.size());
            assertEquals("user", response.getFirst().username());
            assertEquals("Subject", response.getFirst().subject());

        }
    }

    @Nested
    class createChat {
        @Test
        @DisplayName("It should create a new chat with success")
        void success() {
            CreateChatDTO body = new CreateChatDTO("subject",
                    "user@springmail.com",
                    "user@springmail.com",
                    "content");

            User mockUser = new User();
            mockUser.setEmail("user@springmail.com");

            Chat mockChat = new Chat();
            mockChat.setId(UUID.randomUUID());

            when(userRepository.findByEmail(eq(body.sender()))).thenReturn(mockUser);

            doAnswer(invocation -> {
                Chat chat = invocation.getArgument(0);
                chat.setId(UUID.randomUUID());
                return chat;
            }).when(chatRepository).save(any(Chat.class));

            ResponseEntity<Map<String, String>> response = chatService.createChat(body);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Mensagem enviada", response.getBody().get("message"));
        }

        @Test
        @DisplayName("It should fail on create a new chat because subject is empty")
        void errorCase1() {
            CreateChatDTO body = new CreateChatDTO(
                    "",
                    "user@springmail.com",
                    "user@springmail.com",
                    "content");

            ResponseEntity<Map<String, String>> response = chatService.createChat(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Campo assunto não pode estar vazio", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a new chat because destination is empty")
        void errorCase2() {
            CreateChatDTO body = new CreateChatDTO(
                    "subject",
                    "",
                    "user@springmail.com",
                    "content");

            ResponseEntity<Map<String, String>> response = chatService.createChat(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Campo do destinatário não pode estar vazio", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a new chat because sender is empty")
        void errorCase3() {
            CreateChatDTO body = new CreateChatDTO(
                    "subject",
                    "user@springmail.com",
                    "",
                    "content");

            ResponseEntity<Map<String, String>> response = chatService.createChat(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Falha ao localizar seu email", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a new chat because content is empty")
        void errorCase4() {
            CreateChatDTO body = new CreateChatDTO(
                    "subject",
                    "user@springmail.com",
                    "user2@springmail.com",
                    "");

            ResponseEntity<Map<String, String>> response = chatService.createChat(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Mensagem não pode ser vazia", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a new chat because user not found")
        void errorCase5() {
            CreateChatDTO body = new CreateChatDTO(
                    "subject",
                    "user@springmail.com",
                    "user2@springmail.com",
                    "content");

            when(userRepository.findByEmail(body.destination())).thenReturn(null);

            ResponseEntity<Map<String, String>> response = chatService.createChat(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Email não encontrado", response.getBody().get("error"));
        }

        @Test
        @DisplayName("It should fail on create a new chat because user not found")
        void errorCase6() {
            CreateChatDTO body = new CreateChatDTO(
                    "subject",
                    "user@springmail.com",
                    "user2@springmail.com",
                    "content");

            when(userRepository.findByEmail(body.destination())).thenReturn(new User());
            when(userRepository.findByEmail(body.sender())).thenReturn(null);

            ResponseEntity<Map<String, String>> response = chatService.createChat(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Falha ao localizar seu email", response.getBody().get("error"));
        }


    }

    @Nested
    class getByChatId {
        @Test
        @DisplayName("It should return a null value because chat not found")
        void error() {
            UUID id = UUID.randomUUID();

            when(chatRepository.findById(eq(id))).thenReturn(Optional.empty());

            ChatInfoDTO response = chatService.getByChatId(id);

            assertNull(response);
        }

        @Test
        @DisplayName("It should return chat information with success")
        void success() {
            UUID id = UUID.randomUUID();
            Chat chat = new Chat(
                    id,
                    "subject",
                    new User(UUID.randomUUID(), "user", "user@springmail.com", "12345678"),
                    new User(UUID.randomUUID(), "user2", "user2@springmail.com", "12345678"),
                    false, null, null, null
            );

            when(chatRepository.findById(eq(id))).thenReturn(Optional.of(chat));

            ChatInfoDTO response = chatService.getByChatId(id);

            assertNotNull(response);
            assertEquals(chat.getFrom().getEmail() ,response.fromEmail());
            assertEquals(chat.getTo().getEmail(), response.toEmail());
            assertEquals(chat.getSubject(), response.subject());
        }
    }

}