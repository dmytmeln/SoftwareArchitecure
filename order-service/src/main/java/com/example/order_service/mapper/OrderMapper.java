package com.example.order_service.mapper;

import com.example.order_service.domain.Order;
import com.example.order_service.domain.OrderItem;
import com.example.order_service.dto.CartItemDto;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.OrderItemDto;
import com.example.order_service.dto.message.NotificationOrderStatusMessage;
import com.example.order_service.dto.message.OrderCreatedMessage;
import com.example.order_service.dto.message.OrderItemMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);

    @Mapping(target = "productId", source = "cartItem.product.id")
    @Mapping(target = "productName", source = "cartItem.product.name")
    @Mapping(target = "unitPrice", source = "cartItem.product.price")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "orderItemId", ignore = true)
    OrderItem cartItemDtoToOrderItem(CartItemDto cartItem, Order order);

    OrderItemMessage orderItemDtoToOrderItemMessage(OrderItemDto orderItemDto);

    List<OrderItemMessage> orderItemDtoToOrderItemMessage(List<OrderItemDto> orderItemDto);

    @Mapping(target = "orderId", source = "orderDto.id")
    @Mapping(target = "orderItems", source = "orderDto.orderItems")
    OrderCreatedMessage orderDtoToOrderCreatedMessage(OrderDto orderDto);

    default List<OrderItem> cartItemDtoToOrderItem(List<CartItemDto> cartItems, Order order) {
        if (cartItems == null) {
            return null;
        }
        return cartItems.stream()
                .map(cartItem -> cartItemDtoToOrderItem(cartItem, order))
                .toList();
    }

}
