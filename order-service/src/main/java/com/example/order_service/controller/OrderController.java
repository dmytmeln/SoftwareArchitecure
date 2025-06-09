package com.example.order_service.controller;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderServicePublisherWrapper;

    @GetMapping
    public List<OrderDto> getUserOrders(@RequestHeader("X-User-Id") Integer userId) {
        return orderServicePublisherWrapper.getAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestHeader("X-User-Id") Integer userId) {
        return orderServicePublisherWrapper.createOrder(userId);
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable("orderId") Integer orderId, @RequestHeader("X-User-Id") Integer userId) {
        return orderServicePublisherWrapper.getById(orderId, userId);
    }

    @PutMapping("/{orderId}/cancel")
    public OrderDto cancelOrder(@PathVariable("orderId") Integer orderId, @RequestHeader("X-User-Id") Integer userId) {
        return orderServicePublisherWrapper.setCancelledStatus(orderId, userId);
    }

    @PutMapping("/{orderId}/confirm")
    public OrderDto confirmOrder(@PathVariable("orderId") Integer orderId, @RequestHeader("X-User-Id") Integer userId) {
        return orderServicePublisherWrapper.setConfirmedStatus(orderId, userId);
    }

}
