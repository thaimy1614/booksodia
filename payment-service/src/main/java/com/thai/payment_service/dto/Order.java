package com.thai.payment_service.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    private String orderId;
    private String userId;
    private int totalAmount;
    private int totalQuantity;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        DONE,
        REJECTED,
    }
}
