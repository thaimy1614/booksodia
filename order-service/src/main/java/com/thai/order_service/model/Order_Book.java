package com.thai.order_service.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Order_Book {
    private String bookId;

    private String title;

    private int price;

    private int quantity;

    private String image;
}

