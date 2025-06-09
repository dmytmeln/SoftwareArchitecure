package com.example.notification_service.service;

import com.example.notification_service.client.AuthClient;
import com.example.notification_service.dto.NotificationOrderConfirmationMessage;
import com.example.notification_service.dto.NotificationOrderStatusMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {

    @Value("${frontend.url}")
    private String frontend;

    private final JavaMailSender mailSender;
    private final AuthClient authClient;

    public void sendOrderConfirmationEmail(NotificationOrderConfirmationMessage orderConfirmationMessage) {
        var userEmail = authClient.getUserEmail(orderConfirmationMessage.userId());
        sendOrderConfirmationEmail(userEmail, orderConfirmationMessage.orderId());
    }

    public void sendOrderStatusEmail(NotificationOrderStatusMessage orderStatusMessage) {
        var userEmail = authClient.getUserEmail(orderStatusMessage.userId());
        sendOrderStatusEmail(userEmail, orderStatusMessage.orderId(), orderStatusMessage.status());
    }

    public void sendOrderStatusEmail(String to, Integer orderId, String status) {
        log.info("Sending order status email");
        var emailContent = """
                <h1>Order Status Update</h1>
                <p>Your order (ID: %d) status has been updated to: <b>%s</b></p>
                """.formatted(orderId, status);
        sendEmail(to, "Order Status Changed", emailContent);
        // todo add completed and failed messages
    }

    public void sendOrderConfirmationEmail(String to, Integer orderId) {
        log.info("Sending order confirmation email");
        var confirmationLink = frontend + "/confirm-order?orderId=" + orderId;
        var emailContent = """
                <h1>Confirm your Order</h1>
                <a href="%s">Confirm Order</a>
                """.formatted(confirmationLink);
        sendEmail(to, "Order Confirmation Required", emailContent);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            sendEmailHelper(to, subject, content);
        } catch (MessagingException e) {
            throw new IllegalStateException(e);
        }
    }

    private void sendEmailHelper(String to, String subject, String content) throws MessagingException {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

}
