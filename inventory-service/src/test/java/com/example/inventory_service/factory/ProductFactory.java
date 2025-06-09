package com.example.inventory_service.factory;

import com.example.inventory_service.domain.Product;
import com.example.inventory_service.dto.ProductRequest;
import com.example.inventory_service.dto.ProductResponse;

public class ProductFactory {

    public static final Integer PRODUCT_ID = 1;
    public static final String NAME = "name";
    public static final int PRICE = 100;
    public static final int AVAILABLE_STOCK = 1;

    public static Product createProduct() {
        return Product.builder()
                .id(PRODUCT_ID)
                .name(NAME)
                .price(PRICE)
                .availableStock(AVAILABLE_STOCK)
                .build();
    }

    public static ProductResponse createProductResponse() {
        return ProductResponse.builder()
                .id(PRODUCT_ID)
                .name(NAME)
                .price(PRICE)
                .build();
    }

    public static ProductRequest createProductRequest() {
        return ProductRequest.builder()
                .name(NAME)
                .price(PRICE)
                .availableStock(AVAILABLE_STOCK)
                .build();
    }

}
