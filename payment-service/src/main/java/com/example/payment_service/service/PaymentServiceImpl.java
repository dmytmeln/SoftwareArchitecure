package com.example.payment_service.service;

import com.example.payment_service.domain.Payment;
import com.example.payment_service.domain.PaymentStatus;
import com.example.payment_service.dto.PaymentDto;
import com.example.payment_service.exception.NotFoundException;
import com.example.payment_service.mapper.PaymentMapper;
import com.example.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    @Override
    public PaymentDto createPayment(Integer orderId, Integer amount) {
        var payment = Payment.builder()
                .orderId(orderId)
                .amount(amount)
                .build();
        return mapper.toDto(repository.save(payment));
    }

    @Override
    public PaymentDto setConfirmedStatus(Integer paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.CONFIRMED);
    }

    @Override
    public PaymentDto setFailedStatus(Integer paymentId) {
        return updatePaymentStatus(paymentId, PaymentStatus.FAILED);
    }

    private PaymentDto updatePaymentStatus(Integer paymentId, PaymentStatus newStatus) {
        var payment = getExistingById(paymentId);
        payment.setStatus(newStatus);
        return mapper.toDto(repository.save(payment));
    }

    private Payment getExistingById(Integer paymentId) {
        return repository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException(String.format("Payment with id %s not found", paymentId)));
    }

}
