package com.example.notification_service.consumer;

import com.example.notification_service.dto.NotificationOrderConfirmationMessage;
import com.example.notification_service.dto.NotificationOrderStatusMessage;
import com.example.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "notification.order.confirmation.q")
    public void handleOrderConfirmationMessage(NotificationOrderConfirmationMessage orderConfirmationMessage) {
        emailService.sendOrderConfirmationEmail(orderConfirmationMessage);
    }

    @RabbitListener(queues = "notification.order.status.q")
    public void handleOrderStatusMessage(NotificationOrderStatusMessage orderStatusMessage) {
        emailService.sendOrderStatusEmail(orderStatusMessage);
    }

}
