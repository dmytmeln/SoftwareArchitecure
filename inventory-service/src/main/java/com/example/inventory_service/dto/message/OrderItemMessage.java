package com.example.inventory_service.dto.message;

public record OrderItemMessage(
        Integer productId,
        Integer quantity
) {
}
