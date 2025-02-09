package com.gabrielferreira02.springmail.presentation.dto;

import java.util.UUID;

public record ChatInfoDTO(
        UUID id,
        String subject,
        String fromEmail,
        String toEmail) {
}
