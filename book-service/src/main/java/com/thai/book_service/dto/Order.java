package com.thai.book_service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@Builder
public class Order {
    private String orderId;
    private String userId;
    private int totalAmount;
    private int totalQuantity;
    private String status;
    private List<Order_Book> books;

    @JsonCreator
    public Order(@JsonProperty("orderId") String orderId,
                 @JsonProperty("userId") String userId,
                 @JsonProperty("totalAmount") int totalAmount,
                 @JsonProperty("totalQuantity") int totalQuantity,
                 @JsonProperty("status") String status,
                 @JsonProperty("books") List<Order_Book> books
    ) {
        this.books = books;
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.totalQuantity = totalQuantity;
        this.status = status;
    }
}
