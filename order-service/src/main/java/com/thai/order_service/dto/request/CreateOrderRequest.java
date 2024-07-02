package com.thai.order_service.dto.request;

import com.thai.payment_service.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    private String orderId;
    private String userId;
    private List<CartItem> cartItems;
    private int amount;
}
