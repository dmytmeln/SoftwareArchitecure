package com.example.notification_service.dto;

public record NotificationOrderStatusMessage(
        Integer orderId,
        Integer userId,
        String status
) {
}
