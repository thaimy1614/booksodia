package com.thai.payment_service.controller;

import com.thai.payment_service.dto.ResponseObject;
import com.thai.payment_service.dto.request.CartCreationRequest;
import com.thai.payment_service.dto.request.DeleteItemRequest;
import com.thai.payment_service.model.Cart_Book;
import com.thai.payment_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    ResponseObject createCart(@RequestBody CartCreationRequest request) {
        var cart = cartService.addToCart(request);
        return ResponseObject.builder().status("OK").message("CREATE CART SUCCESSFULLY!").data(cart).build();
    }

    @GetMapping("/{id}")
    ResponseObject getCartById(@PathVariable("id") String id) {
        List<Cart_Book> list = cartService.getCart(id);
        return ResponseObject.builder().status("OK").data(list).build();
    }

    @DeleteMapping()
    ResponseObject deleteCartById(@RequestBody DeleteItemRequest request) {
        cartService.removeItems(request);
        return ResponseObject.builder().status("OK").message("REMOVE ITEMS SUCCESSFULLY!").build();
    }
}
