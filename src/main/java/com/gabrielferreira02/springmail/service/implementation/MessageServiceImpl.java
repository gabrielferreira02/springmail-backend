package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.MessageRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.CreateMessageDTO;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import com.gabrielferreira02.springmail.service.interfaces.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(ChatRepository chatRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public ResponseEntity<Map<String, String>> responseMessage(CreateMessageDTO body) {
        Map<String, String> message = new HashMap<>();

        if(body.content().isEmpty()) {
            message.put("error", "Content field cannot be empty");
            return ResponseEntity.badRequest().body(message);
        }

        Optional<User> user = userRepository.findById(UUID.fromString(body.senderId()));

        if(user.isEmpty()) {
            message.put("error", "User id not found");
            return ResponseEntity.badRequest().body(message);
        }

        Optional<Chat> chat = chatRepository.findById(UUID.fromString(body.chatId()));

        if(chat.isEmpty()) {
            message.put("error", "Chat id not found");
            return ResponseEntity.badRequest().body(message);
        }

        Message newMessage = new Message(
                null,
                chat.get(),
                user.get(),
                body.content(),
                null
        );

        messageRepository.save(newMessage);
        message.put("message", "Message send with success");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Override
    public List<MessageDTO> getAllMessagesByChatId(UUID chatId) {
        return messageRepository.findAllMessagesByChatId(chatId);
    }
}
