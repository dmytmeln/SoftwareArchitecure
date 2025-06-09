package com.example.order_service.dto.message;

public record NotificationOrderStatusMessage(
        Integer orderId,
        Integer userId,
        String status
) {
}
