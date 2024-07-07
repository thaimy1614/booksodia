package com.thai.payment_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatus {
    private String status;
    private String orderId;
}
