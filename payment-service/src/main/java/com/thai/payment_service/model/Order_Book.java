package com.thai.payment_service.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Order_Book {
    private String bookId;
    private int quantity;
    private int price;
}

