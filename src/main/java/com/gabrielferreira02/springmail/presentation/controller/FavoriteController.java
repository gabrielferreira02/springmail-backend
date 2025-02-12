package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.ChatDTO;
import com.gabrielferreira02.springmail.presentation.dto.FavoriteDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUsernameDTO;
import com.gabrielferreira02.springmail.service.implementation.FavoriteServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("favorite")
@Tag(name = "Favorite")
public class FavoriteController {

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @GetMapping("{email}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get favorites",
            description = "Return a list of user's favorite chats",
            tags = "Favorite",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Favorite chats returned with success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied"
                    )
            }
    )
    public List<ChatDTO> findFavorites(@PathVariable String email) {
        return favoriteService.findFavorites(email);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get favorites by username",
            description = "Return a list of user's favorite chats filtered by username",
            tags = "Favorite",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Favorite chats returned with success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = List.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied"
                    )
            }
    )
    public List<ChatDTO> findFavoriteChatsByUsername(@RequestParam String username,
                                                     @RequestParam String userEmail) {
        return favoriteService.findFavoriteChatsByUsername(username, userEmail);
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Favorite a chat",
            description = "Endpoint to save a chat in favorites",
            tags = "Favorite",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with chat id and user email",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FavoriteDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Chat saved in favorite with success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ResponseEntity.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied"
                    )
            }
    )
    public ResponseEntity<Object> favorite(@RequestBody FavoriteDTO body) {
        return favoriteService.favorite(body);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Remove chats from favorites",
            description = "Remove a chat from user's favorite",
            tags = "Favorite",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Chat removed from favorites with success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ResponseEntity.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied"
                    )
            }
    )
    public ResponseEntity<Object> removeFavorite(@RequestParam String userEmail, @RequestParam UUID chatId) {
        return favoriteService.removeFavorite(userEmail, chatId);
    }
}
