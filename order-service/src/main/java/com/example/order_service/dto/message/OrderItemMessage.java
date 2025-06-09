package com.example.order_service.dto.message;

public record OrderItemMessage(
        Integer productId,
        Integer quantity
) {
}
