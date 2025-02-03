package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.CreateChatDTO;
import com.gabrielferreira02.springmail.service.implementation.ChatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping
    public ResponseEntity<Map<String, String>> createChat(@RequestBody CreateChatDTO body) {
        return chatService.createChat(body);
    }
}
