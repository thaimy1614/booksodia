package com.thai.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class Order {
    private String orderId;
    private String userId;
    private int totalAmount;
    private int totalQuantity;
    private String status;

    @JsonCreator
    public Order(@JsonProperty("orderId") String orderId,
                 @JsonProperty("userId") String userId,
                 @JsonProperty("totalAmount") int totalAmount,
                 @JsonProperty("totalQuantity") int totalQuantity,
                 @JsonProperty("status") String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.totalQuantity = totalQuantity;
        this.status = status;
    }
}
