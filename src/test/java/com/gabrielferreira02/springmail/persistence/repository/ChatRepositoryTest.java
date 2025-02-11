package com.gabrielferreira02.springmail.persistence.repository;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Favorite;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
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
class ChatRepositoryTest {

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
        Chat chat2 = new Chat(null, "subject", user2, user1, false, null, null, null);
        Message message = new Message(null, chat, user1, "content", null);
        Message message2 = new Message(null, chat2, user2, "content", null);

        userRepository.save(user1);
        this.userId = userRepository.findAll().getFirst().getId();
        userRepository.save(user2);
        chatRepository.save(chat);
        this.chatId = chatRepository.findAll().getFirst().getId();
        chatRepository.save(chat2);
        messageRepository.save(message);
        messageRepository.save(message2);
    }

    @Test
    @DisplayName("It should return chats from user")
    void getAllChatsByUserId() {
        Pageable pageable = PageRequest.of(0, 40);
        List<ChatDTO> chats = chatRepository.getAllChatsByUserId(this.userId, pageable);

        assertEquals(1, chats.size());
        assertEquals("user2", chats.getFirst().username());
        assertEquals("subject", chats.getFirst().subject());
        assertEquals("content", chats.getFirst().message());
    }

    @Test
    @DisplayName("It should return sent chats from user")
    void getAllSentChatsByUserId() {
        Pageable pageable = PageRequest.of(0, 40);
        List<ChatDTO> chats = chatRepository.getAllSentChatsByUserId(this.userId, pageable);

        assertEquals(1, chats.size());
        assertEquals("user", chats.getFirst().username());
        assertEquals("subject", chats.getFirst().subject());
        assertEquals("content", chats.getFirst().message());
    }

    @Test
    @DisplayName("It should return sent chats from user filtered by username")
    void getSentChatsByUsername() {
        String from = "user@springmail.com";
        String to = "user2";
        Pageable pageable = PageRequest.of(0, 40);
        List<ChatDTO> chats = chatRepository.getSentChatsByUsername(from, to, pageable);

        assertEquals(1, chats.size());
        assertEquals("user", chats.getFirst().username());
        assertEquals("subject", chats.getFirst().subject());
        assertEquals("content", chats.getFirst().message());
    }

    @Test
    void deleteByFromId() {
        assertEquals(2, chatRepository.count());
        messageRepository.deleteByChatFromUser(this.userId);
        chatRepository.deleteByFromId(this.userId);
        assertEquals(1, chatRepository.count());
    }

    @Test
    void deleteByToId() {
        assertEquals(2, chatRepository.count());
        messageRepository.deleteByChatFromUser(this.userId);
        chatRepository.deleteByToId(this.userId);
        assertEquals(1, chatRepository.count());
    }
}