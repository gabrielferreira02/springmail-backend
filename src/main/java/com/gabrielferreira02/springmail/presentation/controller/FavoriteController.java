package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.FavoriteDTO;
import com.gabrielferreira02.springmail.service.implementation.FavoriteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("favorite")
public class FavoriteController {

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @GetMapping("{email}")
    @PreAuthorize("hasRole('USER')")
    public List<ChatDTO> findFavorites(@PathVariable String email) {
        return favoriteService.findFavorites(email);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<ChatDTO> findFavoriteChatsByUsername(@RequestParam String username,
                                                     @RequestParam String userEmail) {
        return favoriteService.findFavoriteChatsByUsername(username, userEmail);
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> favorite(@RequestBody FavoriteDTO body) {
        return favoriteService.favorite(body);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> removeFavorite(@RequestParam String userEmail, @RequestParam UUID chatId) {
        return favoriteService.removeFavorite(userEmail, chatId);
    }
}
