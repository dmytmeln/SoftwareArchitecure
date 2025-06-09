package com.example.order_service.dto.message;

import java.util.List;

public record OrderCreatedMessage(
        Integer orderId,
        List<OrderItemMessage> orderItems
) {
}
