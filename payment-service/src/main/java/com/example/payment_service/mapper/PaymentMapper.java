package com.example.payment_service.mapper;

import com.example.payment_service.domain.Payment;
import com.example.payment_service.dto.PaymentDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);

    List<PaymentDto> toDto(List<Payment> payments);

}
