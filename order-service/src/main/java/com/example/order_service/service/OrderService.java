package com.example.order_service.service;

import com.example.order_service.dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllByUserId(Integer userId);

    OrderDto createOrder(Integer userId);

    OrderDto getById(Integer orderId, Integer userId);

    OrderDto setCancelledStatus(Integer orderId, Integer userId);

    OrderDto setConfirmedStatus(Integer orderId, Integer userId);

    OrderDto setCompletedStatus(Integer orderId);

    OrderDto handleReservationFailed(Integer orderId);

    OrderDto handlePaymentFailed(Integer orderId);

    OrderDto setReservedStatus(Integer orderId);
}
