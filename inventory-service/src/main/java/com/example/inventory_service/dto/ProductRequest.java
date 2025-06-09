package com.example.inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder(toBuilder = true)
public record ProductRequest(
        @NotBlank
        String name,

        String description,

        @Positive
        @NotNull
        Integer price,

        @Positive
        @NotNull
        Integer availableStock
) {
}
