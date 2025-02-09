package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.presentation.dto.CreateMessageDTO;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import com.gabrielferreira02.springmail.service.implementation.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("message")
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> responseMessage(@RequestBody CreateMessageDTO body) {
        return messageService.responseMessage(body);
    }

    @GetMapping("chat/{id}")
    @PreAuthorize("hasRole('USER')")
    public List<MessageDTO> getAllMessagesByChatId(@PathVariable UUID id) {
        return messageService.getAllMessagesByChatId(id);
    }
}
