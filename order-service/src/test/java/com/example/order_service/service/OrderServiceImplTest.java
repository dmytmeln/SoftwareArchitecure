package com.example.order_service.service;

import com.example.order_service.domain.Order;
import com.example.order_service.domain.OrderItem;
import com.example.order_service.domain.OrderStatus;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.exception.BadRequestException;
import com.example.order_service.exception.ForbiddenException;
import com.example.order_service.exception.NotFoundException;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.order_service.factory.CartItemFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl service;

    private static final int ID = 1;

    @Test
    void givenOrder_whenGetAll_then_returnListOfOrders() {
        var orders = List.of(getOrder());
        var orderDtos = List.of(getOrderDto());
        when(orderRepository.findAllByUserId(ID)).thenReturn(orders);
        when(orderMapper.toDto(orders)).thenReturn(orderDtos);

        var result = service.getAllByUserId(ID);

        assertEquals(orderDtos, result);
    }

    @Test
    void givenZeroOrders_whenGetAll_then_returnEmptyList() {
        List<Order> orders = List.of();
        when(orderRepository.findAllByUserId(ID)).thenReturn(orders);
        when(orderMapper.toDto(orders)).thenReturn(List.of());

        var result = service.getAllByUserId(ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenCart_whenCreateOrder_thenReturnOrder() {
        var expectedTotalAmount = PRICE * QUANTITY;

        var cartItemDtos = List.of(createCartItemDto());
        var orderDto = getOrderDto();
        var orderItems = List.of(getOrderItem());
        when(cartService.getUserCartItems(USER_ID)).thenReturn(cartItemDtos);
        when(orderMapper.cartItemDtoToOrderItem(eq(cartItemDtos), any(Order.class))).thenReturn(orderItems);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        var result = service.createOrder(USER_ID);

        assertEquals(orderDto, result);

        var orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        var capturedOrder = orderCaptor.getValue();
        assertEquals(USER_ID, capturedOrder.getUserId());
        assertNotNull(capturedOrder.getOrderItems());
        assertEquals(orderItems, capturedOrder.getOrderItems());
        assertEquals(expectedTotalAmount, capturedOrder.getTotalAmount());
    }

    @Test
    void givenEmptyCart_whenCreateOrder_thenThrowException() {
        when(cartService.getUserCartItems(ID)).thenReturn(List.of());

        assertThrows(
                BadRequestException.class,
                () -> service.createOrder(ID));
    }

    @Test
    void givenOrder_whenGetOrder_thenReturnOrder() {
        var order = getOrder();
        var orderDto = getOrderDto();
        when(orderRepository.findById(ID)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);
        Integer orderItemId = ID;
        var result = service.getById(orderItemId, ID);

        assertEquals(orderItemId, result.id());
    }

    @Test
    void givenNoOrder_whenGetOrder_thenThrowException() {
        when(orderRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.getById(ID, ID));
    }

    @Test
    void givenOrderDoesntBelongToUser_whenGetOrder_thenThrowException() {
        var order = getOrder();
        when(orderRepository.findById(ID)).thenReturn(Optional.of(order));

        assertThrows(
                ForbiddenException.class,
                () -> service.getById(ID, 2));
    }

    @Test
    void givenReservedOrder_whenCancelOrder_thenSetCancelledStatus() {
        Integer orderId = ID;

        var order = getOrder(OrderStatus.RESERVED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(getOrderDto());

        var result = service.setCancelledStatus(orderId, ID);

        assertEquals(orderId, result.id());

        var orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderMapper).toDto(orderArgumentCaptor.capture());
        var capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(OrderStatus.CANCELLED, capturedOrder.getStatus());
    }

    @Test
    void givenOrderNotExists_whenSetCancelledStatus_thenThrowException() {
        Integer orderId = ID;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.setCancelledStatus(orderId, ID));
    }

    @Test
    void givenOrderDoesntBelongToUser_whenSetCancelledStatus_thenThrowException() {
        Integer orderId = ID;

        var order = getOrder(OrderStatus.RESERVED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(
                ForbiddenException.class,
                () -> service.setCancelledStatus(orderId, 2));
    }

    @Test
    void givenOrderIsNotReserved_whenSetCancelledStatus_thenThrowException() {
        Integer orderId = ID;

        var order = getOrder();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(
                BadRequestException.class,
                () -> service.setCancelledStatus(orderId, 1));
    }

    @Test
    void givenReservedOrder_whenConfirmOrder_thenSetConfirmedStatus() {
        Integer orderId = ID;

        var order = getOrder(OrderStatus.RESERVED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(getOrderDto());

        var result = service.setConfirmedStatus(orderId, ID);

        assertEquals(orderId, result.id());

        var orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderMapper).toDto(orderArgumentCaptor.capture());
        var capturedOrder = orderArgumentCaptor.getValue();
        assertEquals(OrderStatus.CONFIRMED, capturedOrder.getStatus());
    }

    @Test
    void givenOrderNotExists_whenSetConfirmedStatus_thenThrowException() {
        Integer orderId = ID;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.setConfirmedStatus(orderId, ID));
    }

    @Test
    void givenOrderDoesntBelongToUser_whenSetConfirmedStatus_thenThrowException() {
        Integer orderId = ID;

        var order = getOrder(OrderStatus.RESERVED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(
                ForbiddenException.class,
                () -> service.setConfirmedStatus(orderId, 2));
    }

    @Test
    void givenOrderIsNotReserved_whenSetConfirmedStatus_thenThrowException() {
        Integer orderId = ID;

        var order = getOrder();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(
                BadRequestException.class,
                () -> service.setConfirmedStatus(orderId, 1));
    }

    private Order getOrder(OrderStatus status) {
        var order = getOrder();
        order.setStatus(status);
        return order;
    }

    private Order getOrder() {
        return Order.builder()
                .id(ID)
                .userId(ID)
                .build();
    }

    private OrderDto getOrderDto() {
        return OrderDto.builder()
                .id(ID)
                .build();
    }

    private OrderItem getOrderItem() {
        return OrderItem.builder()
                .productId(ID)
                .productName("name")
                .unitPrice(100)
                .quantity((short) 1)
                .build();
    }

}