package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.domain.CartItem;
import com.example.order_service.domain.CartItemId;
import com.example.order_service.exception.BadRequestException;
import com.example.order_service.exception.NotFoundException;
import com.example.order_service.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.order_service.factory.CartItemFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository repository;

    @Mock
    private ProductClient productClient;

    @InjectMocks
    private CartService service;

    @Test
    void givenCartItems_whenGetCartItems_thenReturnCartItems() {
        var cartItems = List.of(createCartItem());
        var productDto = createProductDto();
        when(repository.findAllByIdUserId(USER_ID)).thenReturn(cartItems);
        when(productClient.getProductById(PRODUCT_ID)).thenReturn(productDto);

        var result = service.getUserCartItems(USER_ID);

        assertFalse(result.isEmpty());
        assertEquals(result.size(), cartItems.size());
        assertEquals(result.get(0).product(), productDto);
    }

    @Test
    void givenNoCartItems_whenGetCartItems_thenReturnEmptyList() {
        when(repository.findAllByIdUserId(USER_ID)).thenReturn(List.of());

        var result = service.getUserCartItems(USER_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenExistingProduct_whenAddProductToCart_thenAddItAndReturnCartItem() {
        when(repository.existsById(new CartItemId(USER_ID, PRODUCT_ID))).thenReturn(false);
        when(productClient.getProductById(PRODUCT_ID)).thenReturn(createProductDto());
        when(repository.save(any(CartItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.addProductToCart(PRODUCT_ID, USER_ID);

        assertEquals(PRODUCT_ID, result.product().id());
    }

    @Test
    void givenProductAlreadyAdded_whenAddProductToCart_thenThrowException() {
        when(repository.existsById(new CartItemId(USER_ID, PRODUCT_ID))).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> service.addProductToCart(PRODUCT_ID, USER_ID));
        verify(repository, times(0)).save(any(CartItem.class));
    }

    @Test
    void givenProductNotExist_whenAddProductToCart_thenThrowException() {
        when(repository.existsById(new CartItemId(USER_ID, PRODUCT_ID))).thenReturn(false);
        when(productClient.getProductById(PRODUCT_ID)).thenReturn(null);

        assertThrows(
                NotFoundException.class,
                () -> service.addProductToCart(PRODUCT_ID, USER_ID));
        verify(repository, times(0)).save(any(CartItem.class));
    }

    @Test
    void givenProductInCart_whenRemoveProductFromCart_thenRemove() {
        when(repository.existsById(new CartItemId(USER_ID, PRODUCT_ID))).thenReturn(true);
        assertDoesNotThrow(() -> service.removeProductFromCart(PRODUCT_ID, USER_ID));
    }

    @Test
    void givenProductIsNotInCart_whenRemoveProductFromCart_thenThrowException() {
        when(repository.existsById(new CartItemId(USER_ID, PRODUCT_ID))).thenReturn(false);
        assertThrows(
                NotFoundException.class,
                () -> service.removeProductFromCart(PRODUCT_ID, USER_ID));
        verify(repository, times(0)).deleteById(any(CartItemId.class));
    }

    @Test
    void givenProductInCart_whenUpdateProductQuantity_thenChangeAndReturnCartItem() {
        var cartItem = createCartItem();
        when(repository.findById(new CartItemId(USER_ID, PRODUCT_ID))).thenReturn(Optional.of(cartItem));
        when(repository.save(cartItem)).thenReturn(cartItem);
        when(productClient.getProductById(PRODUCT_ID)).thenReturn(createProductDto());

        short newQuantity = QUANTITY + 3;
        var cartItemDto = service.changeProductQuantity(PRODUCT_ID, USER_ID, newQuantity);

        assertEquals(newQuantity, cartItemDto.quantity());
    }

    @Test
    void givenInvalidQuantity_whenChangeProductQuantity_thenThrowException() {
        var invalidQuantity = (short) 0;
        assertThrows(
                BadRequestException.class,
                () -> service.changeProductQuantity(PRODUCT_ID, USER_ID, invalidQuantity));
        verify(repository, times(0)).save(any(CartItem.class));
    }

    @Test
    void givenProductNotExist_whenChangeProductQuantity_thenThrowException() {
        when(repository.findById(new CartItemId(USER_ID, PRODUCT_ID))).thenReturn(Optional.empty());
        assertThrows(
                NotFoundException.class,
                () -> service.changeProductQuantity(PRODUCT_ID, USER_ID, QUANTITY));
        verify(repository, times(0)).save(any(CartItem.class));
    }

}