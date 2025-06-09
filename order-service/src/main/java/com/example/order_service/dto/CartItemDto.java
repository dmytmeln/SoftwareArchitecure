package com.example.order_service.dto;

import lombok.Builder;

@Builder
public record CartItemDto(
        ProductDto product,
        Short quantity
) {
}
