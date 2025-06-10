package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.config.db.ReadOnly;
import com.example.order_service.domain.CartItem;
import com.example.order_service.domain.CartItemId;
import com.example.order_service.dto.CartItemDto;
import com.example.order_service.dto.ProductDto;
import com.example.order_service.exception.BadRequestException;
import com.example.order_service.exception.NotFoundException;
import com.example.order_service.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final int QUANTITY_LOWER_BOUND = 1;

    private final CartItemRepository repository;
    private final ProductClient productClient;

    @ReadOnly
    public List<CartItemDto> getUserCartItems(Integer userId) {
        var cartItemDtos = repository.findAllByIdUserId(userId).stream()
                .map(c -> new CartItemDto(
                        productClient.getProductById(c.getId().productId()),
                        c.getQuantity()
                ))
                .toList();

        return cartItemDtos;
    }

    public CartItemDto addProductToCart(Integer productId, Integer userId) {
        verifyDoesntExist(productId, userId);
        var cartItem = CartItem.of(productId, userId);
        var productDto = productClient.getProductById(productId);
        verifyProductExists(productDto);
        var cartItemDb = repository.save(cartItem);
        return new CartItemDto(productDto, cartItemDb.getQuantity());
    }

    public void removeProductFromCart(Integer productId, Integer userId) {
        verifyExists(productId, userId);
        repository.deleteById(new CartItemId(userId, productId));
    }

    public CartItemDto changeProductQuantity(Integer productId, Integer userId, short newQuantity) {
        verifyQuantityIsValid(newQuantity);
        var cartItem = getExisting(productId, userId);
        cartItem.setQuantity(newQuantity);
        var updated = repository.save(cartItem);
        var productDto = productClient.getProductById(productId);
        return new CartItemDto(productDto, updated.getQuantity());
    }

    public void deleteUserCartItems(Integer userId) {
        repository.deleteAllByIdUserId(userId);
    }

    private void verifyQuantityIsValid(short newQuantity) {
        if (newQuantity < QUANTITY_LOWER_BOUND) {
            throw new BadRequestException("Invalid quantity. Must be greater than 0.");
        }
    }

    @ReadOnly
    private CartItem getExisting(Integer productId, Integer userId) {
        return repository.findById(new CartItemId(userId, productId))
                .orElseThrow(() -> new NotFoundException("Product is not in the cart"));
    }

    @ReadOnly
    private void verifyExists(Integer productId, Integer userId) {
        if (!repository.existsById(new CartItemId(userId, productId))) {
            throw new NotFoundException("Product is not in the cart");
        }
    }

    @ReadOnly
    private void verifyDoesntExist(Integer productId, Integer userId) {
        if (repository.existsById(new CartItemId(userId, productId))) {
            throw new BadRequestException("Product already added");
        }
    }

    private void verifyProductExists(ProductDto productDto) {
        if (productDto == null) {
            throw new NotFoundException("Product not found");
        }
    }

}
