package com.example.order_service.dto;

public record OrderItemDto(
        Integer productId,
        String productName,
        Short quantity,
        Integer unitPrice
) {
}
