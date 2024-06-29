package com.thai.payment_service.repository;

import com.thai.payment_service.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, String> {
    Cart findByUserId(String userId);
}
