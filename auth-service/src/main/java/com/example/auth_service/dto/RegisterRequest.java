package com.example.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @Email
        String email,
        @NotBlank
        String fullName,
        @NotBlank
        String password
) {
}
