package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentDto;

public interface PaymentService {
    PaymentDto createPayment(Integer orderId, Integer amount);

    PaymentDto setConfirmedStatus(Integer paymentId);

    PaymentDto setFailedStatus(Integer paymentId);
}
