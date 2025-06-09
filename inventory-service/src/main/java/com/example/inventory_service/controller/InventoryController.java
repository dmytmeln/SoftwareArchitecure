package com.example.inventory_service.controller;

import com.example.inventory_service.dto.InventoryDto;
import com.example.inventory_service.exception.ForbiddenException;
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> getInventory(@RequestHeader("X-User-Role") String role) {
        verifyIsAdmin(role);
        return inventoryService.getInventory();
    }

    private void verifyIsAdmin(String role) {
        if (!role.equals("ROLE_ADMIN")) {
            throw new ForbiddenException("You do not have permission to access this resource");
        }
    }

}
