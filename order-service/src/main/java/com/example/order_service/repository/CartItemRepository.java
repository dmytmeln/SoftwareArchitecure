package com.example.order_service.repository;

import com.example.order_service.domain.CartItem;
import com.example.order_service.domain.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    List<CartItem> findAllByIdUserId(Integer idUserId);

    void deleteAllByIdUserId(Integer idUserId);
}
