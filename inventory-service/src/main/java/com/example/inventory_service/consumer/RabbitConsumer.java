package com.example.inventory_service.consumer;

import com.example.inventory_service.dto.message.InventoryMessage;
import com.example.inventory_service.dto.message.OrderCreatedMessage;
import com.example.inventory_service.dto.message.OrderItemMessage;
import com.example.inventory_service.exception.BadRequestException;
import com.example.inventory_service.publisher.RabbitPublisher;
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RabbitConsumer {

    private final InventoryService inventoryService;
    private final RabbitPublisher rabbitPublisher;

    @RabbitListener(queues = "order.created.q")
    public void handleOrderCreated(OrderCreatedMessage message) {
        try {
            inventoryService.reserveProducts(message.orderItems());
            rabbitPublisher.publishInventoryReserved(new InventoryMessage(message.orderId()));
        } catch (BadRequestException e) {
            rabbitPublisher.publishInventoryFailed(new InventoryMessage(message.orderId()));
        }
        // todo add publisher wrapper for inventory service
    }

    @RabbitListener(queues = "inventory.release.q")
    public void handleInventoryRelease(List<OrderItemMessage> orderItems) {
        inventoryService.releaseProducts(orderItems);
    }

}
