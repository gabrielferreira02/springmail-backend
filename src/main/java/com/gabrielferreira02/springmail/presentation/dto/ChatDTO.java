package com.gabrielferreira02.springmail.presentation.dto;

import java.time.Instant;
import java.util.UUID;

public record ChatDTO(UUID id,
                      String subject,
                      String username,
                      String message,
                      boolean isRead,
                      Instant updatedAt,
                      boolean isFavorite) {
}
