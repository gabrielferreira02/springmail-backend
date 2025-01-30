package com.gabrielferreira02.springmail.presentation.dto;

import java.util.UUID;

public record CreateChatDTO(String subject,
                            String destination,
                            String sender,
                            String content
) {
}
