package com.thai.payment_service.controller;

import com.thai.payment_service.dto.ResponseObject;
import com.thai.payment_service.dto.request.AddToCartRequest;
import com.thai.payment_service.dto.request.DeleteItemRequest;
import com.thai.payment_service.dto.response.ReadCartResponse;
import com.thai.payment_service.model.CartItem;
import com.thai.payment_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    ResponseObject<ReadCartResponse> addToCart(@RequestBody AddToCartRequest request) {
        var cart = cartService.addToCart(request);
        return new ResponseObject<>(HttpStatus.OK, "ADD TO CART SUCCESSFULLY!", cart);
    }

    @GetMapping("/{id}")
    ResponseObject<ReadCartResponse> getCartById(@PathVariable("id") String id) {
        ReadCartResponse list = cartService.readCart(id);
        return new ResponseObject<>(HttpStatus.OK, "GET ALL ITEMS IN CART SUCCESSFULLY!", list);
    }

    @DeleteMapping()
    ResponseObject<ReadCartResponse> deleteCartById(@RequestBody DeleteItemRequest request) {
        cartService.removeFromCart(request);
        return new ResponseObject<>(HttpStatus.OK, "REMOVE ITEMS IN CART SUCCESSFULLY!", null);
    }
}
