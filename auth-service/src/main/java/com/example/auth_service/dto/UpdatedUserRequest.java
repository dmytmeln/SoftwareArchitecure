package com.example.auth_service.dto;

public record UpdatedUserRequest(
        String fullName,
        String email
) {
}
