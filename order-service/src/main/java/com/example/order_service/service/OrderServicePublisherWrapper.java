package com.example.order_service.service;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.message.NotificationOrderConfirmationMessage;
import com.example.order_service.dto.message.NotificationOrderStatusMessage;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.publisher.RabbitPublisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServicePublisherWrapper implements OrderService {

    private final OrderService orderServiceImpl;
    private final RabbitPublisher rabbitPublisher;
    private final OrderMapper mapper;

    @Override
    public List<OrderDto> getAllByUserId(Integer userId) {
        return orderServiceImpl.getAllByUserId(userId);
    }

    @Override
    @Transactional
    public OrderDto createOrder(Integer userId) {
        var orderDto = orderServiceImpl.createOrder(userId);
        var orderCreatedMessage = mapper.orderDtoToOrderCreatedMessage(orderDto);
        rabbitPublisher.publishOrderCreated(orderCreatedMessage);
        return orderDto;
    }

    @Override
    public OrderDto getById(Integer orderId, Integer userId) {
        return orderServiceImpl.getById(orderId, userId);
    }

    @Override
    public OrderDto setConfirmedStatus(Integer orderId, Integer userId) {
        return orderServiceImpl.setConfirmedStatus(orderId, userId);
    }

    @Override
    @Transactional
    public OrderDto setCompletedStatus(Integer orderId) {
        var orderDto = orderServiceImpl.setCompletedStatus(orderId);
        publishNotificationOrderStatus(orderDto);
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto handleReservationFailed(Integer orderId) {
        var orderDto = orderServiceImpl.handleReservationFailed(orderId);
        publishNotificationOrderStatus(orderDto);
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto handlePaymentFailed(Integer orderId) {
        var orderDto = orderServiceImpl.handlePaymentFailed(orderId);
        publishNotificationOrderStatus(orderDto);
        rabbitPublisher.publishInventoryRelease(mapper.orderItemDtoToOrderItemMessage(orderDto.orderItems()));
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto setCancelledStatus(Integer orderId, Integer userId) {
        var orderDto = orderServiceImpl.setCancelledStatus(orderId, userId);
        rabbitPublisher.publishInventoryRelease(mapper.orderItemDtoToOrderItemMessage(orderDto.orderItems()));
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto setReservedStatus(Integer orderId) {
        var orderDto = orderServiceImpl.setReservedStatus(orderId);
        rabbitPublisher.publishNotificationOrderConfirmation(
                new NotificationOrderConfirmationMessage(orderDto.id(), orderDto.userId()));
        return orderDto;
    }

    private void publishNotificationOrderStatus(OrderDto orderDto) {
        rabbitPublisher.publishNotificationOrderStatus(new NotificationOrderStatusMessage(
                orderDto.id(),
                orderDto.userId(),
                orderDto.status()));
    }

}
