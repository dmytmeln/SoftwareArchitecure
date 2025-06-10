package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getAll();

    PaymentDto create(Integer orderId, Integer amount);

    PaymentDto setConfirmedStatus(Integer paymentId);

    PaymentDto setFailedStatus(Integer paymentId);
}
