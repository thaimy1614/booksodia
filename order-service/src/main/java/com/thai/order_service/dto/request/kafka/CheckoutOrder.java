package com.thai.order_service.dto.request.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutOrder {
    private String orderId;
    private int totalAmount;
}
