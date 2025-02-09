package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.FavoriteRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.FavoriteDTO;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Nested
    class FindFavorites {

        @Test
        @DisplayName("It should return a list of favorite chats by user email")
        void success() {
            ChatDTO chat = new ChatDTO(null, "subject", "user", "message", false, null, true);

            when(favoriteRepository.findFavorites(eq("user@springmail.com"), any(Pageable.class))).thenReturn(List.of(chat));

            List<ChatDTO> response = favoriteService.findFavorites("user@springmail.com");

            assertNotNull(response);
            assertEquals(1, response.size());
            assertEquals("subject", response.getFirst().subject());
            assertEquals("user", response.getFirst().username());

        }
    }

    @Nested
    class FindFavoriteChatsByUsername {
        @Test
        @DisplayName("It shoul return chats by username")
        void success() {
            ChatDTO chat = new ChatDTO(null, "subject", "user", "message", false, null, true);

            when(favoriteRepository.findFavoritesByUsername(eq("user@springmail.com"), eq("user"),any(Pageable.class))).thenReturn(List.of(chat));

            List<ChatDTO> response = favoriteService.findFavoriteChatsByUsername("user","user@springmail.com");

            assertNotNull(response);
            assertEquals(1, response.size());
            assertEquals("subject", response.getFirst().subject());
            assertEquals("user", response.getFirst().username());
        }
    }

    @Nested
    class Favorite {
        @Test
        @DisplayName("It should favorite a chat with success")
        void success() {
            UUID chatId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            FavoriteDTO body = new FavoriteDTO(
                    String.valueOf(chatId),
                    "user@springmail.com"
            );

            when(chatRepository.findById(eq(chatId))).thenReturn(Optional.of(new Chat()));
            when(userRepository.findByEmail(eq("user@springmail.com"))).thenReturn(new User(userId, "user", "user@springmail.com", "12345678"));
            when(favoriteRepository.findFavoriteByChatIdAndUserId(eq(chatId), eq(userId))).thenReturn(null);

            ResponseEntity<?> response = favoriteService.favorite(body);

            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because chatId is empty")
        void errorCase1() {
            FavoriteDTO body = new FavoriteDTO(
                    "",
                    "user@springmail.com"
            );

            ResponseEntity<?> response = favoriteService.favorite(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because chatId is empty")
        void errorCase2() {
            UUID chatId = UUID.randomUUID();
            FavoriteDTO body = new FavoriteDTO(
                    String.valueOf(chatId),
                    ""
            );

            ResponseEntity<?> response = favoriteService.favorite(body);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because chat not found")
        void errorCase3() {
            UUID chatId = UUID.randomUUID();
            FavoriteDTO body = new FavoriteDTO(
                    String.valueOf(chatId),
                    "user@springmail.com"
            );

            when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

            ResponseEntity<?> response = favoriteService.favorite(body);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because user not found")
        void errorCase4() {
            UUID chatId = UUID.randomUUID();
            FavoriteDTO body = new FavoriteDTO(
                    String.valueOf(chatId),
                    "user@springmail.com"
            );

            when(chatRepository.findById(eq(chatId))).thenReturn(Optional.of(new Chat()));
            when(userRepository.findByEmail(eq("user@springmail.com"))).thenReturn(null);

            ResponseEntity<?> response = favoriteService.favorite(body);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because chat is already saved")
        void errorCase5() {
            UUID chatId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();
            FavoriteDTO body = new FavoriteDTO(
                    String.valueOf(chatId),
                    "user@springmail.com"
            );

            when(chatRepository.findById(chatId)).thenReturn(Optional.of(new Chat(chatId, "", new User(), new User(), false, null, null, null)));
            when(userRepository.findByEmail(eq("user@springmail.com"))).thenReturn(new User(userId, "", "", ""));
            when(favoriteRepository.findFavoriteByChatIdAndUserId(eq(chatId), eq(userId))).thenReturn(new com.gabrielferreira02.springmail.persistence.entity.Favorite());

            ResponseEntity<?> response = favoriteService.favorite(body);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    class RemoveFavorite {
        @Test
        @DisplayName("It should remove favorite chat with success")
        void success() {
            UUID chatId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();

            when(userRepository.findByEmail(eq("user@springmail.com"))).thenReturn(new User(userId, "user", "user@springmail.com", "12345678"));
            when(favoriteRepository.findFavoriteByChatIdAndUserId(eq(chatId), eq(userId))).thenReturn(new com.gabrielferreira02.springmail.persistence.entity.Favorite());

            ResponseEntity<?> response = favoriteService.removeFavorite("user@springmail.com", chatId);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on remove favorite chat because chatId is empty")
        void errorCase1() {

            ResponseEntity<?> response = favoriteService.removeFavorite("user@springmail.com", null);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because userEmail is empty")
        void errorCase2() {
            UUID chatId = UUID.randomUUID();

            ResponseEntity<?> response = favoriteService.removeFavorite("", chatId);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because  not found")
        void errorCase3() {
            UUID chatId = UUID.randomUUID();
            FavoriteDTO body = new FavoriteDTO(
                    String.valueOf(chatId),
                    "user@springmail.com"
            );

            when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

            ResponseEntity<?> response = favoriteService.favorite(body);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because user not found")
        void errorCase4() {
            UUID chatId = UUID.randomUUID();

            when(userRepository.findByEmail(eq("error@springmail.com"))).thenReturn(null);

            ResponseEntity<?> response = favoriteService.removeFavorite("error@springmail.com", chatId);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("It should fail on favorite a chat because favorite chat not found")
        void errorCase5() {
            UUID chatId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();

            when(userRepository.findByEmail(eq("user@springmail.com"))).thenReturn(new User(userId, "", "", ""));
            when(favoriteRepository.findFavoriteByChatIdAndUserId(eq(chatId), eq(userId))).thenReturn(null);

            ResponseEntity<?> response = favoriteService.removeFavorite("user@springmail.com", chatId);

            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

}