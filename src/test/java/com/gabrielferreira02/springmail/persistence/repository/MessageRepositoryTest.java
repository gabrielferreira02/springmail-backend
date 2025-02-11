package com.gabrielferreira02.springmail.persistence.repository;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @DisplayName("It should return all messages by chat id")
    void findAllMessagesByChatId() {
        User user1 = new User(null, "user", "user@springmail.com", "12345678");
        User user2 = new User(null, "user2", "user2@springmail.com", "12345678");
        Chat chat = new Chat(null, "subject", user1, user2, false, null, null, null);
        Message message = new Message(null, chat, user1, "content", null);

        userRepository.save(user1);
        userRepository.save(user2);
        chatRepository.save(chat);
        messageRepository.save(message);

        List<MessageDTO> messages = messageRepository.findAllMessagesByChatId(chat.getId());

        assertEquals(1, messages.size());
        assertEquals("content", messages.getFirst().content());
        assertEquals(user1.getUsername(), messages.getFirst().senderName());
        assertEquals(user1.getEmail(), messages.getFirst().senderMail());
    }

    @Test
    @DisplayName("It should delete all messages of a user")
    void deleteByChatFromUser() {
        User user1 = new User(null, "user", "user@springmail.com", "12345678");
        User user2 = new User(null, "user2", "user2@springmail.com", "12345678");
        Chat chat = new Chat(null, "subject", user1, user2, false, null, null, null);
        Message message = new Message(null, chat, user1, "content", null);

        userRepository.save(user1);
        userRepository.save(user2);
        chatRepository.save(chat);
        messageRepository.save(message);

        assertEquals(1, messageRepository.count());

        messageRepository.deleteByChatFromUser(user1.getId());

        assertEquals(0, messageRepository.count());

    }

    @Test
    @DisplayName("It should return last message from chat")
    void getLastMessageFromChat() {
        User user1 = new User(null, "user", "user@springmail.com", "12345678");
        User user2 = new User(null, "user2", "user2@springmail.com", "12345678");
        Chat chat = new Chat(null, "subject", user1, user2, false, null, null, null);
        Message message = new Message(null, chat, user1, "content", null);

        userRepository.save(user1);
        userRepository.save(user2);
        chatRepository.save(chat);
        messageRepository.save(message);

        Message lastMessage = messageRepository.getLastMessageFromChat(chat.getId());

        assertEquals("content", lastMessage.getContent());
        assertEquals("user@springmail.com", lastMessage.getSender().getEmail());
        assertEquals("subject", lastMessage.getChat().getSubject());
    }
}