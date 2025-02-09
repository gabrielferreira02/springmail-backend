package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.presentation.dto.*;
import com.gabrielferreira02.springmail.service.implementation.ChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatServiceImpl chatService;

    @GetMapping("user/{userEmail}")
    @PreAuthorize("hasRole('USER')")
    public List<ChatDTO> getChatsByUserId(@PathVariable String userEmail) {
        return chatService.getChatsByUserEmail(userEmail);
    }

    @GetMapping("user/sent/{userEmail}")
    @PreAuthorize("hasRole('USER')")
    public List<ChatDTO> getSentChatsByUserId(@PathVariable String userEmail) {
        return chatService.getSentChatsByUserEmail(userEmail);
    }

    @GetMapping("user/to")
    @PreAuthorize("hasRole('USER')")
    public List<ChatDTO> getSentChatsByUsername(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to
            ) {
        return chatService.getSentChatsByUsername(from, to);
    }

    @GetMapping("user/from")
    @PreAuthorize("hasRole('USER')")
    public List<ChatDTO> getReceivedChatsByUsername(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to
    ) {
        return chatService.getReceivedChatsByUsername(from, to);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ChatInfoDTO getChatById(@PathVariable UUID id) {
        return chatService.getByChatId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> createChat(@RequestBody CreateChatDTO body) {
        return chatService.createChat(body);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> setIsRead(@RequestBody SetIsReadDTO body) {
        chatService.setIsRead(body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
