package com.thai.cart_service.dto.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
    private String bookId;
    private String title;
    private int price;
    private int quantity;
    private String image;
}
