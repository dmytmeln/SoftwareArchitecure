package com.example.payment_service.service;

import com.example.payment_service.domain.PaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCleanupJob {

    private static final String PENDING_NAME = PaymentStatus.PENDING.name();

    private final PaymentService paymentServicePublisherWrapper;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void removeExpiredPendingPayments() {
        var now = LocalDateTime.now();
        var expiredPendingPayments = paymentServicePublisherWrapper.getAll()
                .stream()
                .filter(p -> PENDING_NAME.equals(p.status()))
                .filter(p -> p.createdAt().isBefore(now.minusMinutes(30)))
                .toList();

        expiredPendingPayments.forEach(p -> {
            paymentServicePublisherWrapper.setFailedStatus(p.id());
            log.info("Expired payment with id: {}", p.id());
        });
    }

}
