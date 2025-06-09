package com.example.order_service.controller;

import com.example.order_service.dto.CartItemDto;
import com.example.order_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @GetMapping
    public List<CartItemDto> getUserCart(@RequestHeader("X-User-Id") Integer userId) {
        return service.getUserCartItems(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemDto addProductToCart(@RequestParam Integer productId,
                                        @RequestHeader("X-User-Id") Integer userId
    ) {
        return service.addProductToCart(productId, userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeProductFromCart(@RequestParam Integer productId,
                                      @RequestHeader("X-User-Id") Integer userId
    ) {
        service.removeProductFromCart(productId, userId);
    }

    @PutMapping
    public CartItemDto changeProductQuantity(@RequestParam Integer productId,
                                             @RequestParam Short quantity,
                                             @RequestHeader("X-User-Id") Integer userId
    ) {
        return service.changeProductQuantity(productId, userId, quantity);
    }

}
