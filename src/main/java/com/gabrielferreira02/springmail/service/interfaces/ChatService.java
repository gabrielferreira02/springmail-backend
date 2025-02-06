package com.gabrielferreira02.springmail.service.interfaces;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.CreateChatDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ChatService {
    List<ChatDTO> getChatsByUserEmail(String userEmail);
    List<ChatDTO> getSentChatsByUserEmail(String userEmail);
    List<ChatDTO> getSentChatsByUsername(String from, String to);
    List<ChatDTO> getReceivedChatsByUsername(String from,String to);
    ResponseEntity<Map<String, String>> createChat(CreateChatDTO body);
}
