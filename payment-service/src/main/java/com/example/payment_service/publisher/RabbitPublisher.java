package com.example.payment_service.publisher;

import com.example.payment_service.dto.message.PaymentMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishPaymentSuccess(PaymentMessage paymentMessage) {
        log.info("Sending Payment successful message");
        rabbitTemplate.convertAndSend("payment.exchange", "payment.success", paymentMessage);
    }

    public void publishPaymentFailed(PaymentMessage paymentMessage) {
        log.info("Sending Payment failed message");
        rabbitTemplate.convertAndSend("payment.exchange", "payment.failed", paymentMessage);
    }


}
