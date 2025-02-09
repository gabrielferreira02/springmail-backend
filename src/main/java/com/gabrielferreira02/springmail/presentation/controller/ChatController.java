package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.presentation.dto.*;
import com.gabrielferreira02.springmail.service.implementation.ChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatServiceImpl chatService;

    @GetMapping("user/{userEmail}")
    public List<ChatDTO> getChatsByUserId(@PathVariable String userEmail) {
        return chatService.getChatsByUserEmail(userEmail);
    }

    @GetMapping("user/sent/{userEmail}")
    public List<ChatDTO> getSentChatsByUserId(@PathVariable String userEmail) {
        return chatService.getSentChatsByUserEmail(userEmail);
    }

    @GetMapping("user/to")
    public List<ChatDTO> getSentChatsByUsername(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to
            ) {
        return chatService.getSentChatsByUsername(from, to);
    }

    @GetMapping("user/from")
    public List<ChatDTO> getReceivedChatsByUsername(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to
    ) {
        return chatService.getReceivedChatsByUsername(from, to);
    }

    @GetMapping("{id}")
    public ChatInfoDTO getChatById(@PathVariable UUID id) {
        return chatService.getByChatId(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createChat(@RequestBody CreateChatDTO body) {
        return chatService.createChat(body);
    }

    @PutMapping
    public ResponseEntity<Void> setIsRead(@RequestBody SetIsReadDTO body) {
        chatService.setIsRead(body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
