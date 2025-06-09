package com.example.order_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDto(
        Integer id,
        Integer userId,
        String status,
        Integer totalAmount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<OrderItemDto> orderItems
) {
    @JsonIgnore
    public Integer userId() {
        return userId;
    }
}
