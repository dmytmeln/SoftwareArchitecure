package com.example.auth_service.dto;

import java.time.LocalDateTime;

public record UserResponse(
        Integer id,
        String email,
        String fullName,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
