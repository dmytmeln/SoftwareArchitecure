package com.example.order_service.factory;

import com.example.order_service.domain.CartItem;
import com.example.order_service.domain.CartItemId;
import com.example.order_service.dto.CartItemDto;
import com.example.order_service.dto.ProductDto;

public class CartItemFactory {

    public static final Integer PRODUCT_ID = 1;
    public static final Integer USER_ID = 1;
    public static final String NAME = "name";
    public static final int PRICE = 100;
    public static final short QUANTITY = (short) 1;

    public static CartItem createCartItem() {
        return CartItem.builder()
                .id(new CartItemId(USER_ID, PRODUCT_ID))
                .quantity(QUANTITY)
                .build();
    }

    public static CartItemDto createCartItemDto() {
        return CartItemDto.builder()
                .product(createProductDto())
                .quantity(QUANTITY)
                .build();
    }

    public static ProductDto createProductDto() {
        return ProductDto.builder()
                .id(PRODUCT_ID)
                .name(NAME)
                .price(PRICE)
                .build();
    }

}
