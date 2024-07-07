package com.thai.order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thai.order_service.dto.PaymentStatus;
import com.thai.order_service.dto.request.OrderCreationRequest;
import com.thai.order_service.dto.request.kafka.CheckoutOrder;
import com.thai.order_service.dto.request.kafka.InitCartCheckout;
import com.thai.order_service.mapper.OrderMapper;
import com.thai.order_service.model.Order;
import com.thai.order_service.model.Order_Book;
import com.thai.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Order createOrder(OrderCreationRequest request) {
        final int[] totalAmount = {0};
        request.getBooks().forEach(book -> totalAmount[0] += book.getPrice() * book.getQuantity());
        final int[] totalQuantity = {0};
        request.getBooks().forEach(book -> totalQuantity[0] += book.getQuantity());
        Order order = Order.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .books(request.getBooks())
                .totalAmount(totalAmount[0])
                .totalQuantity(totalQuantity[0])
                .status(Order.Status.PENDING)
                .build();
        // TODO: SEND EVENT ORDER_CREATED
        return orderRepository.save(order);
    }

    public void updateOrder(Order.Status status, String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }

    public Order getOrderByOrderId(String orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @KafkaListener(id = "init-cart-checkout-group", topics = "init-cart-checkout")
    public void initCartCheckout(InitCartCheckout initCartCheckout) throws JsonProcessingException {
        List<Order_Book> orderBooks = orderMapper.toOrderBook(initCartCheckout.getBooks());
        Order order = createOrder(OrderCreationRequest.builder().userId(initCartCheckout.getUserId()).orderId(initCartCheckout.getOrderId()).books(orderBooks).build());
        String orderJson = objectMapper.writeValueAsString(order);
        log.info(orderJson);
        redisTemplate.opsForValue().set("order:" + order.getOrderId(), orderJson);
        redisTemplate.expire("order:" + order.getOrderId(), 5, TimeUnit.MINUTES);
        kafkaTemplate.send("created-order", CheckoutOrder.builder().orderId(order.getOrderId()).totalAmount(order.getTotalAmount()).build());
    }

    @KafkaListener(id = "update-order-group", topics = "payment-status")
    public void updateOrderStatus(PaymentStatus paymentStatus) {
        
    }
}
