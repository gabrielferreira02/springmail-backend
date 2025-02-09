package com.gabrielferreira02.springmail.presentation.dto;

import java.util.UUID;

public record SetIsReadDTO(String email,
                           UUID chatId) {
}
