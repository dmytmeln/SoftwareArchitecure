package com.example.inventory_service.dto;

import lombok.Builder;

@Builder
public record ProductResponse(
        Integer id,
        String name,
        String description,
        int price
) {
}
