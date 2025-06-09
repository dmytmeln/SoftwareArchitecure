package com.example.inventory_service.publisher;

import com.example.inventory_service.dto.message.InventoryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.example.inventory_service.config.RabbitConfig.INVENTORY_EXCHANGE;

@Service
@RequiredArgsConstructor
@Log4j2
public class RabbitPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishInventoryReserved(InventoryMessage inventoryMessage) {
        log.info("Sending inventory reserved message");
        rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, "inventory.reserved", inventoryMessage);
    }

    public void publishInventoryFailed(InventoryMessage inventoryMessage) {
        log.info("Sending inventory failed message");
        rabbitTemplate.convertAndSend(INVENTORY_EXCHANGE, "inventory.failed", inventoryMessage);
    }

}
