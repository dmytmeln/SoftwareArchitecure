package com.example.order_service.consumer;

import com.example.order_service.dto.message.InventoryMessage;
import com.example.order_service.dto.message.PaymentMessage;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitConsumer {

    private final OrderService orderServicePublisherWrapper;

    @RabbitListener(queues = "inventory.reserved.q")
    public void handleInventoryReserved(InventoryMessage inventoryMessage) {
        orderServicePublisherWrapper.setReservedStatus(inventoryMessage.orderId());
    }

    @RabbitListener(queues = "inventory.failed.q")
    public void handleInventoryFailed(InventoryMessage inventoryMessage) {
        orderServicePublisherWrapper.handleReservationFailed(inventoryMessage.orderId());
    }

    @RabbitListener(queues = "payment.success.q")
    public void handlePaymentSuccess(PaymentMessage paymentMessage) {
        orderServicePublisherWrapper.setCompletedStatus(paymentMessage.orderId());
    }

    @RabbitListener(queues = "payment.failed.q")
    public void handlePaymentFailed(PaymentMessage paymentMessage) {
        orderServicePublisherWrapper.handlePaymentFailed(paymentMessage.orderId());
    }

}
