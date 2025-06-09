package com.example.order_service.dto.message;

public record NotificationOrderConfirmationMessage(
        Integer orderId,
        Integer userId
) {
}
