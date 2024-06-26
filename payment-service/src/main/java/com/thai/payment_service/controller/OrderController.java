package com.thai.payment_service.controller;

import com.thai.payment_service.dto.ResponseObject;
import com.thai.payment_service.dto.request.OrderCreationRequest;
import com.thai.payment_service.model.Order;
import com.thai.payment_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    ResponseObject<Order> createOrder(@RequestBody OrderCreationRequest request) {
        var order = orderService.createOrder(request);
        return new ResponseObject<>(HttpStatus.OK, "CREATE ORDER SUCCESSFULLY!", order);
    }
}
