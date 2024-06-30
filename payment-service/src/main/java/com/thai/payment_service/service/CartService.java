package com.thai.payment_service.service;

import com.thai.payment_service.dto.request.AddToCartRequest;
import com.thai.payment_service.dto.request.DeleteItemRequest;
import com.thai.payment_service.dto.response.ReadCartResponse;
import com.thai.payment_service.model.CartItem;
import com.thai.payment_service.model.CartItemKey;
import com.thai.payment_service.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;

    @Transactional
    public ReadCartResponse addToCart(AddToCartRequest request) {
        CartItemKey key = new CartItemKey(request.getUserId(), request.getBookId());

        boolean exists = cartRepository.existsById(key);

        if (!exists) {
            CartItem cartItem = CartItem.builder()
                    .userId(request.getUserId())
                    .bookId(request.getBookId())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .build();

            cartRepository.save(cartItem);
        } else {
            // TODO: throw new?
            log.info("Item already exists");
        }

        List<CartItem> updatedCartItems = cartRepository.findCartItemsByUserId(request.getUserId());

        return ReadCartResponse.builder()
                .cart(updatedCartItems)
                .build();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void removeFromCart(DeleteItemRequest request) throws Exception {
        List<CartItem> beforeDelete = cartRepository.findCartItemsByUserId(request.getUserId());
        if (request.getUserId() == null || request.getBookId() == null || request.getBookId().isEmpty()) {
            throw new IllegalArgumentException("UserId and BookId list must be provided");
        }
        cartRepository.deleteByUserIdAndBookIdIn(request.getUserId(), request.getBookId());
        List<CartItem> afterDelete = cartRepository.findCartItemsByUserId(request.getUserId());
        if(beforeDelete.size() == afterDelete.size()) {
            throw new Exception("BOOKS NOT IN CART");
        }
        ReadCartResponse.builder().cart(afterDelete).build();
    }

    public ReadCartResponse readCart(String id) {
        return ReadCartResponse.builder()
                .cart(cartRepository.findCartItemsByUserId(id))
                .build();
    }
}
