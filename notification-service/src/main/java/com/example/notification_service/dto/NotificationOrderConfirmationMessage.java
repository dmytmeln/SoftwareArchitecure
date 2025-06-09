package com.example.notification_service.dto;

public record NotificationOrderConfirmationMessage(
        Integer orderId,
        Integer userId
) {
}
