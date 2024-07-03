package com.thai.order_service.service;

import com.thai.order_service.dto.request.OrderCreationRequest;
import com.thai.order_service.dto.request.kafka.InitCartCheckout;
import com.thai.order_service.mapper.OrderMapper;
import com.thai.order_service.model.Order;
import com.thai.order_service.model.Order_Book;
import com.thai.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

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
    public void initCartCheckout(InitCartCheckout initCartCheckout){
        List<Order_Book> orderBooks = orderMapper.toOrderBook(initCartCheckout.getBooks());
        createOrder(OrderCreationRequest.builder().userId(initCartCheckout.getUserId()).orderId("ABC").books(orderBooks).build());

    }
}
