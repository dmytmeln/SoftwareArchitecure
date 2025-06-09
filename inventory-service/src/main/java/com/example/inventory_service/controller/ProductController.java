package com.example.inventory_service.controller;

import com.example.inventory_service.dto.ProductRequest;
import com.example.inventory_service.dto.ProductResponse;
import com.example.inventory_service.exception.ForbiddenException;
import com.example.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final InventoryService service;

    @GetMapping
    public List<ProductResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@RequestBody @Valid ProductRequest request,
                                  @RequestHeader("X-User-Role") String role
    ) {
        verifyIsAdmin(role);
        return service.create(request);
    }

    @GetMapping("/{productId}")
    public ProductResponse getById(@PathVariable Integer productId) {
        return service.getById(productId);
    }

    @PutMapping("/{productId}")
    public ProductResponse update(@PathVariable Integer productId,
                                  @RequestBody @Valid ProductRequest request,
                                  @RequestHeader("X-User-Role") String role
    ) {
        verifyIsAdmin(role);
        return service.update(productId, request);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer productId, @RequestHeader("X-User-Role") String role) {
        verifyIsAdmin(role);
        service.delete(productId);
    }

    private void verifyIsAdmin(String role) {
        if (!role.equals("ROLE_ADMIN")) {
            throw new ForbiddenException("You do not have permission to access this resource");
        }
    }

}
