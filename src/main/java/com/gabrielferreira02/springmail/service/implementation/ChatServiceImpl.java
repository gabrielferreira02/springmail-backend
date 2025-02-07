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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
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
            log.warn("User email, {}, not found", userEmail);
            return List.of();
        }

        log.info("Returned chats for user: {}", user.getId());
        return chatRepository.getAllChatsByUserId(user.getId(), pageable);
    }

    @Override
    public List<ChatDTO> getSentChatsByUserEmail(String userEmail) {
        Pageable pageable = PageRequest.of(0, 40);
        User user = userRepository.findByEmail(userEmail);

        if(user == null) {
            log.warn("User email, {}, not found", userEmail);
            return List.of();
        }

        log.info("Returned sent chats for user: {}", user.getId());
        return chatRepository.getAllSentChatsByUserId(user.getId(), pageable);
    }

    @Override
    public List<ChatDTO> getSentChatsByUsername(String from, String to) {
        Pageable pageable = PageRequest.of(0, 40);
        log.info("Returned sent chats for user {} to the following search: {}", from, to);
        return chatRepository.getSentChatsByUsername(from ,to, pageable);
    }

    @Override
    public List<ChatDTO> getReceivedChatsByUsername(String from, String to) {
        Pageable pageable = PageRequest.of(0, 40);
        log.info("Returned received chats for user {} to the following search: {}", to, from);
        return chatRepository.getReceivedChatsByUsername(from ,to, pageable);
    }

    @Override
    public ResponseEntity<Map<String, String>> createChat(CreateChatDTO body) {
        Map<String, String> message = new HashMap<>();

        if(body.subject().isEmpty()) {
            log.error("Field subject is empty");
            message.put("error", "Campo assunto não pode estar vazio");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.destination().isEmpty()) {
            log.error("Field destination is empty");
            message.put("error", "Campo do destinatário não pode estar vazio");
            return ResponseEntity.badRequest().body(message);
        }

        User destination = userRepository.findByEmail(body.destination());
        if(destination == null) {
            log.error("User destination not found: {}", body.destination());
            message.put("error", "Email não encontrado");
            return ResponseEntity.badRequest().body(message);
        }

        if(body.sender().isEmpty()) {
            log.error("User email is empty");
            message.put("error", "Falha ao localizar seu email");
            return ResponseEntity.badRequest().body(message);
        }

        User sender = userRepository.findByEmail(body.sender());
        if(sender == null) {
            log.error("User not found: {}", body.sender());
            message.put("error", "Falha ao localizar seu email");
            return ResponseEntity.badRequest().body(message);
        }

        Chat newChat = new Chat(
                null,
                body.subject(),
                sender,
                destination,
                false,
                null,
                null,
                null
        );

        Chat createdChat = chatRepository.save(newChat);
        log.info("Chat created between users {} and {} with id: {}", body.sender(), body.destination(), createdChat.getId());
        Message newMessage = new Message(
                null,
                createdChat,
                sender,
                body.content(),
                null
        );

        messageRepository.save(newMessage);
        message.put("message", "Mensagem enviada");
        log.info("New message sent with success");
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
