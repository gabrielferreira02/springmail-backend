package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.Favorite;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.FavoriteDTO;
import com.gabrielferreira02.springmail.service.implementation.FavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("favorite")
public class FavoriteController {

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @GetMapping("{email}")
    public List<ChatDTO> findFavorites(@PathVariable String email) {
        return favoriteService.findFavorites(email);
    }

    @GetMapping
    public List<ChatDTO> findFavoriteChatsByUsername(@RequestParam String username,
                                                     @RequestParam String userEmail) {
        return favoriteService.findFavoriteChatsByUsername(username, userEmail);
    }

    @PostMapping()
    public ResponseEntity<?> favorite(@RequestBody FavoriteDTO body) {
        return favoriteService.favorite(body);
    }

    @DeleteMapping()
    public ResponseEntity<?> removeFavorite(@RequestParam String userEmail, @RequestParam UUID chatId) {
        return favoriteService.removeFavorite(userEmail, chatId);
    }
}
