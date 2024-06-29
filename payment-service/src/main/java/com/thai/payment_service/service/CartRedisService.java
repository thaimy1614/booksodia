package com.thai.payment_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thai.payment_service.model.Cart;
import com.thai.payment_service.model.Cart_Book;
import com.thai.payment_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final CartRepository cartRepository;
    private final ObjectMapper redisObjectMapper;

    public void deleteAll() {
        List<String> keys = new ArrayList<>();
        List<Cart> carts = cartRepository.findAll();
        carts.forEach(cart -> {
            keys.add("cart_" + cart.getUserId());
        });
        redisTemplate.delete(keys);
    }

    public void saveCart(Cart cart) throws JsonProcessingException {
        String key = "cart_" + cart.getUserId();
        String json = redisObjectMapper.writeValueAsString(cart);
        redisTemplate.opsForValue().set(key, json);
    }

    public List<Cart_Book> getCartByUserId(String userId) throws JsonProcessingException {
        String json = (String) redisTemplate.opsForValue().get("cart_" + userId);
        return (json != null) ? redisObjectMapper.readValue(json, new TypeReference<List<Cart_Book>>() {
        }) : null;
    }
}
