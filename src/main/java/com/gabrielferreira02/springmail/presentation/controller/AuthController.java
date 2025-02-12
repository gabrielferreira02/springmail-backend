package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.presentation.dto.CreateUserDTO;
import com.gabrielferreira02.springmail.presentation.dto.LoginRequestDTO;
import com.gabrielferreira02.springmail.service.implementation.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
@Tag(name = "Authentication", description = "Include routes for login and register a user")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PreAuthorize("permitAll()")
    @PostMapping("login")
    @Operation(
            summary = "Login a user",
            description = "Authenticate a user an return a token jwt and user email",
            tags = "Authentication",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with user email and password",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ResponseEntity.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO body) {
        return authService.login(body);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("register")
    @Operation(
            summary = "Register a user",
            description = "Create a new user",
            tags = "Authentication",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with username, email and password \n *Email must be only the first part of email address",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateUserDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created with success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ResponseEntity.class
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Map<String, String>> register(@RequestBody CreateUserDTO body) {
        return authService.register(body);
    }

}
