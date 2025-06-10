package com.example.payment_service.service;

import com.example.payment_service.dto.PaymentDto;
import com.example.payment_service.dto.message.PaymentMessage;
import com.example.payment_service.publisher.RabbitPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServicePublisherWrapper implements PaymentService {

    private final PaymentService paymentServiceImpl;
    private final RabbitPublisher rabbitPublisher;

    @Override
    public List<PaymentDto> getAll() {
        return paymentServiceImpl.getAll();
    }

    @Override
    public PaymentDto create(Integer orderId, Integer amount) {
        return paymentServiceImpl.create(orderId, amount);
    }

    @Override
    public PaymentDto setConfirmedStatus(Integer paymentId) {
        var paymentDto = paymentServiceImpl.setConfirmedStatus(paymentId);
        rabbitPublisher.publishPaymentSuccess(new PaymentMessage(paymentDto.orderId()));
        return paymentDto;
    }

    @Override
    public PaymentDto setFailedStatus(Integer paymentId) {
        var paymentDto = paymentServiceImpl.setFailedStatus(paymentId);
        rabbitPublisher.publishPaymentFailed(new PaymentMessage(paymentDto.orderId()));
        return paymentDto;
    }

}
