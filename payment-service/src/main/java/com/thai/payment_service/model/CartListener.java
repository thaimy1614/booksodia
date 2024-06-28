package com.thai.payment_service.model;

import com.thai.payment_service.service.CartRedisService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public class CartListener {
    private final CartRedisService cartRedisService;

    @PostUpdate
    public void update(Cart cart) {
        cartRedisService.deleteAll();
    }

    @PostPersist
    public void persist(Cart cart) {
        cartRedisService.deleteAll();
    }
}
