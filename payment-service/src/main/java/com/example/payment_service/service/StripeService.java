package com.example.payment_service.service;

import com.example.payment_service.exception.InvalidSignatureException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class StripeService {

    private static final String USD_CURRENCY = "usd";
    private static final String PAYMENT_ID_KEY = "paymentId";

    private final PaymentService paymentServicePublisherWrapper;

    @Value("${stripe.api.secret.key}")
    private String secretKey;
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(Integer orderId, Integer amount) {
        var payment = paymentServicePublisherWrapper.create(orderId, amount);

        var metadata = Map.of(
                "orderId", orderId.toString(),
                PAYMENT_ID_KEY, payment.id().toString());
        var params = PaymentIntentCreateParams.builder()
                .setAmount(Long.valueOf(amount))
                .setCurrency(USD_CURRENCY)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )

                .putAllMetadata(metadata)
                .build();
        return createPaymentIntent(params);
    }

    public void handeWebhookEvent(String payload, String signature) {
        var event = constructWebhookEvent(payload, signature);
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handleSuccessfulPayment(event);
            case "payment_intent.payment_failed" -> handleFailedPayment(event);
            default -> log.warn("Unhandled event type: {}", event.getType());
        }
    }

    private PaymentIntent createPaymentIntent(PaymentIntentCreateParams params) {
        try {
            return PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new IllegalStateException(e);
        }
    }

    private Event constructWebhookEvent(String payload, String signature) {
        try {
            return Webhook.constructEvent(payload, signature, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new InvalidSignatureException("Invalid signature ", e);
        }
    }

    private void handleSuccessfulPayment(Event event) {
        var paymentIntent = getPaymentIntentFromWebhookEvent(event);
        var paymentId = getPaymentIdFromPaymentIntent(paymentIntent);
        log.info("Payment succeeded for PaymentIntent: {} and Payment: {}", paymentIntent.getId(), paymentId);
        paymentServicePublisherWrapper.setConfirmedStatus(paymentId);
    }

    private void handleFailedPayment(Event event) {
        var paymentIntent = getPaymentIntentFromWebhookEvent(event);
        var paymentId = getPaymentIdFromPaymentIntent(paymentIntent);
        log.info("Payment failed for PaymentIntent: {} and Payment: {}", paymentIntent.getId(), paymentId);
        paymentServicePublisherWrapper.setFailedStatus(paymentId);
    }

    private PaymentIntent getPaymentIntentFromWebhookEvent(Event webhookEvent) {
        return (PaymentIntent) webhookEvent.getDataObjectDeserializer()
                .getObject()
                .get();
    }

    private Integer getPaymentIdFromPaymentIntent(PaymentIntent paymentIntent) {
        return Integer.valueOf(paymentIntent.getMetadata().get(PAYMENT_ID_KEY));
    }

}
