package com.gabrielferreira02.springmail.presentation.dto;

public record CreateMessageDTO(
        String chatId,
        String senderId,
        String content
) {
}
