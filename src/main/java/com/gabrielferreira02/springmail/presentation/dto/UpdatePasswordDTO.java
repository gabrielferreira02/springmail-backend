package com.gabrielferreira02.springmail.presentation.dto;

public record UpdatePasswordDTO(String newPassword, String oldPassword, String email) {
}
