package com.thai.cart_service.service;

import com.thai.cart_service.dto.request.AddToCartRequest;
import com.thai.cart_service.dto.request.CheckoutRequest;
import com.thai.cart_service.dto.request.DeleteItemRequest;
import com.thai.cart_service.dto.response.CheckoutResponse;
import com.thai.cart_service.dto.response.ReadCartResponse;

public interface CartService {
    ReadCartResponse addToCart(AddToCartRequest request);

    public void removeFromCart(DeleteItemRequest request) throws Exception;

    public ReadCartResponse readCart(String userId);

    public CheckoutResponse checkout(CheckoutRequest request);
}
