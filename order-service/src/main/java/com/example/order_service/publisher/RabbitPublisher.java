package com.example.order_service.publisher;

import com.example.order_service.dto.message.NotificationOrderConfirmationMessage;
import com.example.order_service.dto.message.NotificationOrderStatusMessage;
import com.example.order_service.dto.message.OrderCreatedMessage;
import com.example.order_service.dto.message.OrderItemMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishNotificationOrderConfirmation(NotificationOrderConfirmationMessage confirmationMessage) {
        log.info("Sending notification order confirmation message");
        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "notification.order.confirmation",
                confirmationMessage);
    }

    public void publishNotificationOrderStatus(NotificationOrderStatusMessage orderStatusMessage) {
        log.info("Sending notification order status message");
        rabbitTemplate.convertAndSend(
                "notification.exchange",
                "notification.order.status",
                orderStatusMessage);
    }

    public void publishOrderCreated(OrderCreatedMessage orderCreatedMessage) {
        log.info("Sending order created message");
        rabbitTemplate.convertAndSend("order.exchange", "order.created", orderCreatedMessage);
    }

    public void publishInventoryRelease(List<OrderItemMessage> orderItems) {
        log.info("Sending inventory release message");
        rabbitTemplate.convertAndSend("inventory.exchange", "inventory.release", orderItems);
    }

}
