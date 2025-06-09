package com.example.payment_service.controller;

import com.example.payment_service.service.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/payment-intent")
    public Map<String, String> createPaymentIntent(@RequestParam Integer orderId,
                                                   @RequestParam Integer amount
    ) {
        var paymentIntent = stripeService.createPaymentIntent(orderId, amount);
        return Map.of("clientSecret", paymentIntent.getClientSecret());
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String signature
    ) {
        stripeService.handeWebhookEvent(payload, signature);
        return ResponseEntity.ok("Webhook received");
    }

}
