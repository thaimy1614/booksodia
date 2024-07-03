package com.thai.cart_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private String bookId;
    private int price;
    private int quantity;
}
