package com.gabrielferreira02.springmail.service.interfaces;

import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.CreateChatDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChatService {
    List<ChatDTO> getChatsByUserId(@PathVariable UUID userId);
    ResponseEntity<Map<String, String>> createChat(@RequestBody CreateChatDTO body);
}
