package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.Chat;
import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.*;
import com.gabrielferreira02.springmail.service.implementation.ChatServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("chat")
@Tag(name = "Chat", description = "Routes to manager chats")
public class ChatController {

    @Autowired
    private ChatServiceImpl chatService;

    @GetMapping("user/{userEmail}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get chats from user",
            description = "Return a received chat list by user id",
            tags = "Chat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Received chats returned with success",
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
    public List<ChatDTO> getChatsByUserId(@PathVariable String userEmail) {
        return chatService.getChatsByUserEmail(userEmail);
    }

    @GetMapping("user/sent/{userEmail}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get sent chats from user",
            description = "Return a sent chat list by user id",
            tags = "Chat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sent chats returned with success",
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
    public List<ChatDTO> getSentChatsByUserId(@PathVariable String userEmail) {
        return chatService.getSentChatsByUserEmail(userEmail);
    }

    @GetMapping("user/to")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get sent chats from user filtered",
            description = "Return a sent chat list from user filtered by destination username",
            tags = "Chat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Sent chats returned with success",
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
    public List<ChatDTO> getSentChatsByUsername(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to
            ) {
        return chatService.getSentChatsByUsername(from, to);
    }

    @GetMapping("user/from")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get received chats from user filtered",
            description = "Return a received chat list from user filtered by sender username",
            tags = "Chat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Received chats returned with success",
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
    public List<ChatDTO> getReceivedChatsByUsername(
            @RequestParam(name = "from") String from,
            @RequestParam(name = "to") String to
    ) {
        return chatService.getReceivedChatsByUsername(from, to);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get chat",
            description = "Return chat information by chat id",
            tags = "Chat",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Chat data returned with success",
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
    public ChatInfoDTO getChatById(@PathVariable UUID id) {
        return chatService.getByChatId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Create chat",
            description = "Endpoint to create a new chat conversation with any user",
            tags = "Chat",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with subject, content, sender email and destination email",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateChatDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Chat created with success",
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
    public ResponseEntity<Map<String, String>> createChat(@RequestBody CreateChatDTO body) {
        return chatService.createChat(body);
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Read chat",
            description = "Endpoint to set a chat as read",
            tags = "Chat",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with user email and chat id",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateChatDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Chat updated",
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
    public ResponseEntity<Void> setIsRead(@RequestBody SetIsReadDTO body) {
        chatService.setIsRead(body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
