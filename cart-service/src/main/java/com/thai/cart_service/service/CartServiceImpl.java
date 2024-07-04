package com.thai.cart_service.service;

import com.thai.cart_service.dto.kafka.InitCartCheckout;
import com.thai.cart_service.dto.request.AddToCartRequest;
import com.thai.cart_service.dto.request.CheckoutRequest;
import com.thai.cart_service.dto.request.DeleteItemRequest;
import com.thai.cart_service.dto.request.client.GetBookRequest;
import com.thai.cart_service.dto.response.BookResponse;
import com.thai.cart_service.dto.response.CheckoutResponse;
import com.thai.cart_service.dto.response.ReadCartResponse;
import com.thai.cart_service.mapper.CartMapper;
import com.thai.cart_service.dto.kafka.Book;
import com.thai.cart_service.model.CartItem;
import com.thai.cart_service.model.CartItemKey;
import com.thai.cart_service.repository.CartRepository;
import com.thai.cart_service.repository.httpclient.BookClient;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private static final String CART_KEY_PREFIX = "cart:";
    private final CartRepository cartRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CartMapper cartMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BookClient bookClient;

    private String getCartKey(String userId) {
        return CART_KEY_PREFIX + userId;
    }

    @Transactional
    public ReadCartResponse addToCart(AddToCartRequest request) {
        CartItemKey key = new CartItemKey(request.getUserId(), request.getBookId());

        boolean exists = cartRepository.existsById(key);

        if (!exists) {
            BookResponse bookResponse = bookClient.getBook(GetBookRequest.builder().bookId(request.getBookId()).build());
            if (bookResponse.getQuantity() >= request.getQuantity()) {
                CartItem cartItem = cartMapper.toCartItem(bookResponse);
                cartItem.setUserId(request.getUserId());
                cartItem.setQuantity(request.getQuantity());
                cartItem.setCreateAt(null);
                cartItem.setBookId(bookResponse.getId());
                cartRepository.save(cartItem);
                List<CartItem> updatedCartItems = cartRepository.findCartItemsByUserId(request.getUserId());
                addItemsToRedis(updatedCartItems, cartItem.getUserId());
                return ReadCartResponse.builder()
                        .cart(updatedCartItems)
                        .build();
            } else {
                log.info("No have enough book to add to cart");
            }
        } else {
            // TODO: throw new?
            log.info("Item already exists");
        }

        return null;
    }

    private void addItemsToRedis(List<CartItem> items, String userId) {
        String cartKey = getCartKey(userId);
        redisTemplate.opsForHash().putAll(cartKey, items.stream()
                .collect(Collectors.toMap(CartItem::getBookId, cartItem -> cartItem)));
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void removeFromCart(DeleteItemRequest request) throws Exception {
        String cartKey = getCartKey(request.getUserId());

        List<CartItem> beforeDelete = cartRepository.findCartItemsByUserId(request.getUserId());
        if (request.getUserId() == null || request.getBookId() == null || request.getBookId().isEmpty()) {
            throw new IllegalArgumentException("UserId and BookId list must be provided");
        }
        cartRepository.deleteByUserIdAndBookIdIn(request.getUserId(), request.getBookId());
        List<CartItem> afterDelete = cartRepository.findCartItemsByUserId(request.getUserId());
        if (beforeDelete.size() == afterDelete.size()) {
            throw new Exception("BOOKS NOT IN CART");
        }

        request.getBookId().forEach(bookId -> redisTemplate.opsForHash().delete(cartKey, bookId));
    }

    public ReadCartResponse readCart(String userId) {
        String cartKey = getCartKey(userId);
        List<Object> cartItemsFromRedis = redisTemplate.opsForHash().values(cartKey);

        if (cartItemsFromRedis.isEmpty()) {
            // Fallback to database if Redis cache is empty
            List<CartItem> cartItemsFromDb = cartRepository.findCartItemsByUserId(userId);

            // Store the items in Redis cache
            addItemsToRedis(cartItemsFromDb, userId);

            return ReadCartResponse.builder().cart(cartItemsFromDb).build();
        }

        List<CartItem> cartItems = cartItemsFromRedis.stream()
                .map(item -> (CartItem) item)
                .collect(Collectors.toList());

        return ReadCartResponse.builder().cart(cartItems).build();
    }

    public CheckoutResponse checkout(CheckoutRequest request) {
        String orderId = UUID.randomUUID().toString();
        // Get all items in card
        List<CartItem> cartItems = cartRepository.findCartItemsByUserId(request.getUserId());
        List<Book> books = cartMapper.toBookList(cartItems);
        kafkaTemplate.send("init-cart-checkout", InitCartCheckout.builder().orderId(orderId).userId(request.getUserId()).books(books).build());
        return CheckoutResponse.builder().orderId(orderId).build();
    }
}
