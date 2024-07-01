package com.thai.payment_service.service;

import com.thai.payment_service.dto.request.AddToCartRequest;
import com.thai.payment_service.dto.request.CheckoutRequest;
import com.thai.payment_service.dto.request.DeleteItemRequest;
import com.thai.payment_service.dto.request.OrderCreationRequest;
import com.thai.payment_service.dto.response.ReadCartResponse;
import com.thai.payment_service.model.CartItem;
import com.thai.payment_service.model.CartItemKey;
import com.thai.payment_service.model.Order_Book;
import com.thai.payment_service.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private static final String CART_KEY_PREFIX = "cart:";
    private final CartRepository cartRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final OrderService orderService;

    private String getCartKey(String userId) {
        return CART_KEY_PREFIX + userId;
    }

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
            List<CartItem> updatedCartItems = cartRepository.findCartItemsByUserId(request.getUserId());
            addItemsToRedis(updatedCartItems, cartItem.getUserId());
            return ReadCartResponse.builder()
                    .cart(updatedCartItems)
                    .build();
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

    public void checkout(CheckoutRequest request) {
        // Get all items in card
        List<CartItem> cartItems = readCart(request.getUserId()).getCart();
        // TODO: send a message "createOrder" to Order service
        List<Order_Book> orderBooks = convertToOrderBookList(cartItems);
        orderService.createOrder(OrderCreationRequest.builder()
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .books(orderBooks)
                .build());
    }

    public Order_Book convertToOrderBook(CartItem cartItem) {
        Order_Book orderBook = new Order_Book();
        orderBook.setBookId(cartItem.getBookId());
        orderBook.setQuantity(cartItem.getQuantity());
        orderBook.setPrice(cartItem.getPrice().intValue());
        return orderBook;
    }

    public List<Order_Book> convertToOrderBookList(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::convertToOrderBook)
                .collect(Collectors.toList());
    }

}
