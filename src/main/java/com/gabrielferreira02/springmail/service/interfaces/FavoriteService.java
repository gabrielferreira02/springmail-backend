package com.gabrielferreira02.springmail.service.interfaces;

import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.FavoriteDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface FavoriteService {
    List<ChatDTO> findFavorites(String email);
    List<ChatDTO> findFavoriteChatsByUsername(String username, String userEmail);
    ResponseEntity<?> favorite(FavoriteDTO body);
    ResponseEntity<?> removeFavorite(String userEmail, UUID chatId);
}
