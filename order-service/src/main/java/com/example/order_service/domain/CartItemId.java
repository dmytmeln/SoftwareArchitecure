package com.example.order_service.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record CartItemId(Integer userId, Integer productId) {
    public CartItemId {
    }
}
