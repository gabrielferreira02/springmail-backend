package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.Message;
import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.CreateMessageDTO;
import com.gabrielferreira02.springmail.presentation.dto.MessageDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdatePasswordDTO;
import com.gabrielferreira02.springmail.service.implementation.MessageServiceImpl;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("message")
@Tag(name = "Message", description = "Routes to manage messages from a chat")
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Create message",
            description = "Create a new message in reply form within a chat",
            tags = "Message",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with chat id, sender email and message content",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateMessageDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created new message with success",
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
    public ResponseEntity<Map<String, String>> responseMessage(@RequestBody CreateMessageDTO body) {
        return messageService.responseMessage(body);
    }

    @GetMapping("chat/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get messages",
            description = "Return a list of all messages from a chat by id",
            tags = "Message",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Chat messages returned with success",
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
    public List<MessageDTO> getAllMessagesByChatId(@PathVariable UUID id) {
        return messageService.getAllMessagesByChatId(id);
    }
}
