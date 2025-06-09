package com.example.order_service.service;

import com.example.order_service.domain.Order;
import com.example.order_service.domain.OrderItem;
import com.example.order_service.domain.OrderStatus;
import com.example.order_service.dto.CartItemDto;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.exception.BadRequestException;
import com.example.order_service.exception.ForbiddenException;
import com.example.order_service.exception.NotFoundException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
    public List<OrderDto> getAllByUserId(Integer userId) {
        return mapper.toDto(orderRepository.findAllByUserId(userId));
    }

    @Override
    @Transactional
    public OrderDto createOrder(Integer userId) {
        var cartItemDtos = cartService.getUserCartItems(userId);
        verifyNotEmpty(cartItemDtos);
        var order = Order.of(userId);
        var orderItems = mapper.cartItemDtoToOrderItem(cartItemDtos, order);
        int orderTotalAmount = calculateTotalAmount(orderItems);
        order.setOrderItems(orderItems);
        order.setTotalAmount(orderTotalAmount);
        return mapper.toDto(orderRepository.save(order));
        // todo delete exception handler for Exception in RestControllerInterceptor
    }

    @Override
    public OrderDto getById(Integer orderId, Integer userId) {
        var order = getExistingById(orderId);
        verifyBelongsToUser(order, userId);
        return mapper.toDto(order);
    }

    @Override
    public OrderDto setCancelledStatus(Integer orderId, Integer userId) {
        return transitionReservedOrderStatus(orderId, userId, OrderStatus.CANCELLED);
    }

    @Override
    public OrderDto setConfirmedStatus(Integer orderId, Integer userId) {
        return transitionReservedOrderStatus(orderId, userId, OrderStatus.CONFIRMED);
    }

    @Override
    @Transactional
    public OrderDto setCompletedStatus(Integer orderId) {
        var order = getExistingById(orderId);
        order.setStatus(OrderStatus.COMPLETED);
        cartService.deleteUserCartItems(order.getUserId());
        return mapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto handleReservationFailed(Integer orderId) {
        return changeStatus(orderId, OrderStatus.RESERVATION_FAILED);
    }

    @Override
    public OrderDto handlePaymentFailed(Integer orderId) {
        return changeStatus(orderId, OrderStatus.PAYMENT_FAILED);
    }

    @Override
    public OrderDto setReservedStatus(Integer orderId) {
        return changeStatus(orderId, OrderStatus.RESERVED);
    }

    private OrderDto changeStatus(Integer orderId, OrderStatus newStatus) {
        var order = getExistingById(orderId);
        order.setStatus(newStatus);
        return mapper.toDto(orderRepository.save(order));
    }

    private void verifyNotEmpty(List<CartItemDto> cartItemDtos) {
        if (cartItemDtos.isEmpty()) {
            throw new BadRequestException("Shopping cart is empty");
        }
    }

    private int calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToInt(OrderItem::calculateTotalAmount)
                .sum();
    }

    private OrderDto transitionReservedOrderStatus(Integer orderId, Integer userId, OrderStatus newStatus) {
        var order = getExistingById(orderId);
        verifyBelongsToUser(order, userId);
        verifyIsReserved(order);
        order.setStatus(newStatus);
        return mapper.toDto(orderRepository.save(order));
    }

    private Order getExistingById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    private void verifyBelongsToUser(Order order, Integer userId) {
        if (!order.getUserId().equals(userId)) {
            throw new ForbiddenException("Order does not belong to user");
        }
    }

    private void verifyIsReserved(Order order) {
        if (order.getStatus() != OrderStatus.RESERVED) {
            throw new BadRequestException("Order status can't be changed");
        }
    }

}
