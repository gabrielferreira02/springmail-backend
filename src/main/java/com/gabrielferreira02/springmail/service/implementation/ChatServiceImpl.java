package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.MessageRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.CreateChatDTO;
import com.gabrielferreira02.springmail.service.interfaces.ChatService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public ChatServiceImpl(UserRepository userRepository, MessageRepository messageRepository, ChatRepository chatRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public List<ChatDTO> getChatsByUserEmail(String userEmail) {
        Pageable pageable = PageRequest.of(0, 40);
        User user = userRepository.findByEmail(userEmail);

        if(user == null) {
            return List.of();
        }

        return chatRepository.getAllChatsByUserId(user.getId(), pageable);
    }

    @Override
    public ResponseEntity<Map<String, String>> createChat(CreateChatDTO body) {
        Map<String, String> message = new HashMap<>();

        if(body.subject().isEmpty()) {
            message.put("error", "Campo assunto não pode estar vazio");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.destination().isEmpty()) {
            message.put("error", "Campo do destinatário não pode estar vazio");
            return ResponseEntity.badRequest().body(message);
        }

        User destination = userRepository.findByEmail(body.destination());
        if(destination == null) {
            message.put("error", "Email não encontrado");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.sender().isEmpty()) {
            message.put("error", "Falha ao localizar seu email");
            return ResponseEntity.badRequest().body(message);
        }

        User sender = userRepository.findByEmail(body.sender());
        if(sender == null) {
            message.put("error", "Falha ao localizar seu email");
            return ResponseEntity.badRequest().body(message);
        }

        Chat newChat = new Chat(
                null,
                body.subject(),
                destination,
                sender,
                false,
                null,
                null,
                null
        );

        Chat createdChat = chatRepository.save(newChat);

        Message newMessage = new Message(
                null,
                createdChat,
                sender,
                body.content(),
                null
        );

        messageRepository.save(newMessage);

        message.put("message", "Mensagem enviada");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
