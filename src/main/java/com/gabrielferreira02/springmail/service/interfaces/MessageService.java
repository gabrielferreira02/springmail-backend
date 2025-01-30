package com.gabrielferreira02.springmail.service.interfaces;

import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.presentation.dto.CreateMessageDTO;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessageService {
    ResponseEntity<Map<String, String>> responseMessage(CreateMessageDTO body);
    List<MessageDTO> getAllMessagesByChatId(UUID chatId);
}
