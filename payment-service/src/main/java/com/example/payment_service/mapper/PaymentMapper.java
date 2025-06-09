package com.example.payment_service.mapper;

import com.example.payment_service.domain.Payment;
import com.example.payment_service.dto.PaymentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentDto toDto(Payment payment);

}
