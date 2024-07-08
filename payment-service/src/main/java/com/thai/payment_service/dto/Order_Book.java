package com.thai.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order_Book {
    private String bookId;

    private String title;

    private int price;

    private int quantity;

    private String image;
}

