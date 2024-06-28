package com.thai.payment_service.service;

import com.thai.payment_service.dto.request.CartCreationRequest;
import com.thai.payment_service.model.Cart;
import com.thai.payment_service.model.Cart_Book;
import com.thai.payment_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Cart addToCart(CartCreationRequest request) {
        Optional<Cart> cart = cartRepository.findById(request.getUserId());

        if (cart.isEmpty()) {
            cart = Optional.ofNullable(Cart.builder()
                    .userId(request.getUserId())
                    .books(List.of(request.getBook()))
                    .build());
        } else {
            List<Cart_Book> books = cart.get().getBooks();
            books.add(request.getBook());
            cart.get().setBooks(books);
        }
        return cartRepository.save(cart.orElseThrow());
    }
}
