package com.example.inventory_service.service;

import com.example.inventory_service.domain.Product;
import com.example.inventory_service.dto.InventoryDto;
import com.example.inventory_service.dto.ProductRequest;
import com.example.inventory_service.dto.ProductResponse;
import com.example.inventory_service.dto.message.OrderItemMessage;
import com.example.inventory_service.exception.BadRequestException;
import com.example.inventory_service.exception.NotFoundException;
import com.example.inventory_service.mapper.InventoryMapper;
import com.example.inventory_service.mapper.ProductMapper;
import com.example.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final InventoryMapper inventoryMapper;

    public List<ProductResponse> getAll() {
        return productMapper.toResponse(productRepository.findAll());
    }

    public List<InventoryDto> getInventory() {
        return inventoryMapper.toDto(productRepository.findAll());
    }

    public ProductResponse create(ProductRequest productRequest) {
        verifyNotExistsByName(productRequest);
        var entity = productMapper.toEntity(productRequest);
        return productMapper.toResponse(productRepository.save(entity));
    }

    public ProductResponse getById(Integer productId) {
        return productMapper.toResponse(getExisting(productId));
    }

    public ProductResponse update(Integer productId, ProductRequest productRequest) {
        var productDb = getExisting(productId);
        if (!productDb.getName().equals(productRequest.name())) {
            verifyNotExistsByName(productRequest);
        }
        var entity = productMapper.toEntity(productRequest, productId);
        return productMapper.toResponse(productRepository.save(entity));
    }

    public void delete(Integer productId) {
        var product = getExisting(productId);
        productRepository.delete(product);
    }

    @Transactional(rollbackFor = BadRequestException.class)
    public void reserveProducts(List<OrderItemMessage> orderItems) {
        orderItems.forEach(this::reserveProduct);
    }

    private void reserveProduct(OrderItemMessage orderItem) {
        log.info("Reserving {} Products {}", orderItem.quantity(), orderItem.productId());
        var product = getExisting(orderItem.productId());
        if (product.getAvailableStock() < orderItem.quantity()) {
            throw new BadRequestException("Not enough stock to release product");
        }
        product.removeItems(orderItem.quantity());
        productRepository.save(product);
    }

    public void releaseProducts(List<OrderItemMessage> orderItems) {
        orderItems.forEach(this::releaseProduct);
    }

    private void releaseProduct(OrderItemMessage orderItem) {
        log.info("Releasing {} Products {}", orderItem.quantity(), orderItem.productId());
        var product = getExisting(orderItem.productId());
        product.addItems(orderItem.quantity());
        productRepository.save(product);
        // todo optimize by creating special query
    }

    private Product getExisting(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product with such id not found"));
    }

    private void verifyNotExistsByName(ProductRequest productRequest) {
        if (productRepository.existsByName(productRequest.name())) {
            throw new BadRequestException("Product with such name already exists");
        }
    }

}
