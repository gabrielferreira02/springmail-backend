package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
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

    @PostMapping
    public ResponseEntity<Map<String, String>> createChat(@RequestBody CreateChatDTO body) {
        return chatService.createChat(body);
    }
}
