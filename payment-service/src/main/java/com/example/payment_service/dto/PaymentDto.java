package com.example.payment_service.dto;

import java.time.LocalDateTime;

public record PaymentDto(
        Integer id,
        Integer orderId,
        Integer amount,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
