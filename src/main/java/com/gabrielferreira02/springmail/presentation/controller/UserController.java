package com.gabrielferreira02.springmail.presentation.controller;

import com.gabrielferreira02.springmail.persistence.entity.User;
import com.gabrielferreira02.springmail.presentation.dto.LoginRequestDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdatePasswordDTO;
import com.gabrielferreira02.springmail.presentation.dto.UpdateUsernameDTO;
import com.gabrielferreira02.springmail.service.implementation.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Tag(name = "User", description = "Routes for user account management")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("{email}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "User information",
            description = "Return user data by user email identification",
            tags = "User",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User data returned with success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = User.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied"
                    )
            }
    )
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("username")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Update username",
            description = "Endpoint to update username",
            tags = "User",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with username and email",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateUsernameDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Username updated with success",
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
    public ResponseEntity<Void> updateUsername(@RequestBody UpdateUsernameDTO body) {
        return userService.updateUsername(body);
    }

    @PutMapping("password")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Update password",
            description = "Endpoint to update user password",
            tags = "User",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Body request with new password, old password and email",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdatePasswordDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password updated with success",
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
    public ResponseEntity<Object> updatePassword(@RequestBody UpdatePasswordDTO body) {
        return userService.updatePassword(body);
    }

    @DeleteMapping("{email}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Delete user",
            description = "Delete a user by email",
            tags = "User",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "User deleted with success",
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
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        return userService.deleteUser(email);
    }
}
