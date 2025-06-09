package com.example.inventory_service.dto;

import java.time.LocalDateTime;

public record InventoryDto(
        Integer id,
        String name,
        Integer availableStock,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
