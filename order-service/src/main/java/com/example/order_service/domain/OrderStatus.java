package com.example.order_service.domain;

public enum OrderStatus {
    PENDING,
    RESERVED,
    RESERVATION_FAILED,
    PAYMENT_FAILED,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}
