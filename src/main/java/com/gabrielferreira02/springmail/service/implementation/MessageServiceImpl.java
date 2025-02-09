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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
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
            log.error("Message content is empty");
            message.put("error", "Content field cannot be empty");
            return ResponseEntity.badRequest().body(message);
        }

        User user = userRepository.findByEmail(body.senderEmail());

        if(user == null) {
            log.error("User not found");
            message.put("error", "User not found");
            return ResponseEntity.badRequest().body(message);
        }

        Optional<Chat> chat = chatRepository.findById(UUID.fromString(body.chatId()));

        if(chat.isEmpty()) {
            log.error("Chat not found");
            message.put("error", "Chat id not found");
            return ResponseEntity.badRequest().body(message);
        }

        Message newMessage = new Message(
                null,
                chat.get(),
                user,
                body.content(),
                null
        );

        
        chat.get().setRead(false);
        chatRepository.save(chat.get());
        messageRepository.save(newMessage);
        log.info("Message sent with success in chat: {}", chat.get().getId());
        message.put("message", "Message send with success");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @Override
    public List<MessageDTO> getAllMessagesByChatId(UUID chatId) {
        log.info("Returned all message for chat: {}", chatId);
        return messageRepository.findAllMessagesByChatId(chatId);
    }
}
