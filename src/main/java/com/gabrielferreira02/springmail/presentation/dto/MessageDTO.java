package com.gabrielferreira02.springmail.presentation.dto;

import java.time.Instant;

public record MessageDTO(
        String senderName,
        String senderMail,
        Instant createdAt,
        String content
) {
}
