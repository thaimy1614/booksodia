package com.thai.payment_service.repository;

import com.thai.payment_service.model.CartItem;
import com.thai.payment_service.model.CartItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, CartItemKey> {
    List<CartItem> findCartItemsByUserId(String userId);

    void deleteByUserIdAndBookIdIn(String userId, List<String> bookId);
}
