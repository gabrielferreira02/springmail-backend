package com.gabrielferreira02.springmail.persistence.repository;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Favorite;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FavoriteRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    private UUID userId;
    private UUID chatId;

    @BeforeEach
    void setup() {
        User user1 = new User(null, "user", "user@springmail.com", "12345678");
        User user2 = new User(null, "user2", "user2@springmail.com", "12345678");
        Chat chat = new Chat(null, "subject", user1, user2, false, null, null, null);
        Message message = new Message(null, chat, user1, "content", null);
        Favorite favorite = new Favorite(null, chat, user1);

        userRepository.save(user1);
        this.userId = userRepository.findAll().getFirst().getId();
        userRepository.save(user2);
        chatRepository.save(chat);
        this.chatId = chatRepository.findAll().getFirst().getId();
        messageRepository.save(message);
        favoriteRepository.save(favorite);
    }


    @Test
    void findFavoriteByChatIdAndUserId() {
        Favorite favorite = favoriteRepository.findFavoriteByChatIdAndUserId(this.chatId, this.userId);

        assertEquals("subject", favorite.getChat().getSubject());
        assertEquals("user", favorite.getUser().getUsername());
        assertEquals("user@springmail.com", favorite.getUser().getEmail());
    }

    @Test
    @DisplayName("It should return a list of favorite chats of a user")
    void findFavorites() {
        Pageable pageable = PageRequest.of(0, 40);
        List<ChatDTO> favorites = favoriteRepository.findFavorites("user@springmail.com", pageable);

        assertEquals(1, favorites.size());
        assertEquals("subject", favorites.getFirst().subject());
        assertEquals("user", favorites.getFirst().username());
    }

    @Test
    @DisplayName("It should return a favorite chats list filtered by username")
    void findFavoritesByUsername() {
        String userEmail = "user@springmail.com";
        String username = "user";
        Pageable pageable = PageRequest.of(0, 40);

        List<ChatDTO> favorites = favoriteRepository.findFavoritesByUsername(userEmail, username, pageable);

        assertEquals(1, favorites.size());
        assertEquals("content", favorites.getFirst().message());
        assertEquals("subject", favorites.getFirst().subject());
    }

    @Test
    @DisplayName("It should delete all favorite chats by user id")
    void deleteFavoritesByChat() {
        assertEquals(1, favoriteRepository.count());

        favoriteRepository.deleteFavoritesByChat(this.userId);

        assertEquals(0, favoriteRepository.count());
    }
}