package com.example.order_service.client;

import com.example.order_service.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "INVENTORY-SERVICE", dismiss404 = true)
public interface ProductClient {

    @GetMapping("/api/v1/products/{productId}")
    ProductDto getProductById(@PathVariable Integer productId);

}
