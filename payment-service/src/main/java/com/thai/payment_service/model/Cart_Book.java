package com.thai.payment_service.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import lombok.Data;

@Data
@Embeddable
@EntityListeners(CartListener.class)
public class Cart_Book {
    private String bookId;
    private int quantity;
    private int price;
}
