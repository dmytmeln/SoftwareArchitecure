package com.example.inventory_service.service;

import com.example.inventory_service.domain.Product;
import com.example.inventory_service.exception.BadRequestException;
import com.example.inventory_service.exception.NotFoundException;
import com.example.inventory_service.mapper.ProductMapper;
import com.example.inventory_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.inventory_service.factory.ProductFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private InventoryService service;

    @Test
    void givenProducts_whenGetProducts_thenReturnProducts() {
        var products = List.of(createProduct());
        var productResponses = List.of(createProductResponse());
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toResponse(products)).thenReturn(productResponses);

        var result = service.getAll();

        assertFalse(result.isEmpty());
        assertEquals(productResponses, result);
    }

    @Test
    void givenNoProducts_whenGetProducts_thenReturnEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());
        when(productMapper.toResponse(List.of())).thenReturn(List.of());

        var result = service.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void givenProductRequest_whenCreate_thenCreateAndReturnProduct() {
        var productRequest = createProductRequest();
        var product = createProduct();
        var productResponse = createProductResponse();
        when(productRepository.existsByName(product.getName())).thenReturn(false);
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        var result = service.create(productRequest);

        assertNotNull(result);
        assertEquals(productResponse, result);
    }

    @Test
    void givenProductRequestWithNameThatAlreadyExists_whenCreate_thenThrowException() {
        var productRequest = createProductRequest();
        when(productRepository.existsByName(productRequest.name())).thenReturn(true);
        assertThrows(
                BadRequestException.class,
                () -> service.create(productRequest));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void givenProduct_whenGetById_thenReturnProduct() {
        var product = createProduct();
        var productResponse = createProductResponse();
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        var result = service.getById(PRODUCT_ID);

        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.id());
    }

    @Test
    void givenProductNotExist_whenGetById_thenThrowException() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.getById(PRODUCT_ID));
    }

    @Test
    void givenProductRequest_whenUpdate_thenUpdateAndReturnProduct() {
        var productRequest = createProductRequest();
        var product = createProduct();
        var productResponse = createProductResponse();
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productMapper.toEntity(productRequest, PRODUCT_ID)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        var result = service.update(PRODUCT_ID, productRequest);

        assertNotNull(result);
        assertEquals(productResponse, result);
    }

    @Test
    void givenProductRequestWithNameThatAlreadyExists_whenUpdate_thenThrowException() {
        var productRequest = createProductRequest().toBuilder()
                .name("New name")
                .build();
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(createProduct()));
        when(productRepository.existsByName(productRequest.name())).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> service.update(PRODUCT_ID, productRequest));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void givenProductNotExists_whenUpdate_thenThrowException() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.update(PRODUCT_ID, createProductRequest()));
    }

    @Test
    void givenProduct_whenDelete_thenDeleteProduct() {
        var product = createProduct();
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> service.delete(PRODUCT_ID));

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void givenProductNotExists_whenDelete_thenThrowException() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.delete(PRODUCT_ID));
    }

}