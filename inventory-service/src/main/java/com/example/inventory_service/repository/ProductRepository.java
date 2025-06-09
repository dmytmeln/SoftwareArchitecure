package com.example.inventory_service.repository;

import com.example.inventory_service.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);
}
