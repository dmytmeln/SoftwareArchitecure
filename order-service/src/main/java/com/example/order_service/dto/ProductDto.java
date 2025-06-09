package com.example.order_service.dto;

import lombok.Builder;

@Builder
public record ProductDto(
        Integer id,
        String name,
        Integer price
) {
}
