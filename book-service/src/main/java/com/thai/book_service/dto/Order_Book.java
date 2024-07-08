package com.thai.book_service.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Order_Book {
    private String bookId;

    private String title;

    private int price;

    private int quantity;

    private String image;
}

