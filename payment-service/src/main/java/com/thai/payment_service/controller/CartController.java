package com.thai.payment_service.controller;

import com.thai.payment_service.dto.ResponseObject;
import com.thai.payment_service.dto.request.OrderCreationRequest;
import com.thai.payment_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    ResponseObject createOrder(@RequestBody CartCreationRequest request) {
        var cart = cartService.createCart(request);
        return ResponseObject.builder().status("OK").message("CREATE ORDER SUCCESSFULLY!").data(order).build();
    }
}
