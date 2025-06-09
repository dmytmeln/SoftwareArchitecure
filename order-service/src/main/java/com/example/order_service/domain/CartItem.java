package com.example.order_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @EmbeddedId
    private CartItemId id;

    @Column(name = "quantity", nullable = false)
    private Short quantity;

    public static CartItem of(Integer productId, Integer userId) {
        return new CartItem(new CartItemId(userId, productId), (short) 1);
    }

}
