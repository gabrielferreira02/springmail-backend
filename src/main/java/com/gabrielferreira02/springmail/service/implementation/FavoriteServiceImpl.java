package com.gabrielferreira02.springmail.service.implementation;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Favorite;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.persistence.repository.ChatRepository;
import com.gabrielferreira02.springmail.persistence.repository.FavoriteRepository;
import com.gabrielferreira02.springmail.persistence.repository.UserRepository;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.FavoriteDTO;
import com.gabrielferreira02.springmail.service.interfaces.FavoriteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public FavoriteServiceImpl(FavoriteRepository favoriteRepository, ChatRepository chatRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ChatDTO> findFavorites(String email) {
        Pageable pageable = PageRequest.of(0, 40);
        return favoriteRepository.findFavorites(email, pageable);
    }

    @Override
    public List<ChatDTO> findFavoriteChatsByUsername(String username, String userEmail) {
        Pageable pageable = PageRequest.of(0, 40);
        return favoriteRepository.findFavoritesByUsername(userEmail, username, pageable);
    }

    @Override
    public ResponseEntity<?> favorite(FavoriteDTO body) {
        Map<String, String> response = new HashMap<>();

        if(body.chatId().isEmpty()) {
            response.put("message", "Campo chatId não pode estar vazio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if(body.userEmail().isEmpty()) {
            response.put("message", "Campo email de usuario não pode estar vazio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        UUID chatId = UUID.fromString(body.chatId());
        Optional<Chat> chat = chatRepository.findById(chatId);
        User user = userRepository.findByEmail(body.userEmail());

        if(chat.isEmpty()) {
            response.put("message", "Chat id não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if(user == null) {
            response.put("message", "Usuario id não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Favorite favorite = favoriteRepository.findFavoriteByChatIdAndUserId(
                chatId,
                user.getId()
        );

        if(favorite != null) {
            response.put("message", "Esse chat já está em seus favoritos");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


        Favorite newFavorite = new Favorite(null, chat.get(), user);
        return ResponseEntity.ok(favoriteRepository.save(newFavorite));
    }

    @Override
    public ResponseEntity<?> removeFavorite(String userEmail, UUID chatId) {
        Map<String, String> response = new HashMap<>();

        if(chatId == null) {
            response.put("message", "Campo chatId não pode estar vazio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if(userEmail.isEmpty()) {
            response.put("message", "Campo email do ususario não pode estar vazio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        User user = userRepository.findByEmail(userEmail);

        if(user == null) {
            response.put("message", "Usuario não encontrado");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Favorite favorite = favoriteRepository.findFavoriteByChatIdAndUserId(
                chatId,
                user.getId()
        );

        if(favorite == null) {
            response.put("message", "Esse chat não está em seus favoritos");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        favoriteRepository.deleteById(favorite.getId());
        return ResponseEntity.noContent().build();
    }
}
