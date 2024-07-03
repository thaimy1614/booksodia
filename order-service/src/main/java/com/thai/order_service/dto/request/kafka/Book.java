package com.thai.order_service.dto.request.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private String bookId;

    private String title;

    private int price;

    private int quantity;

    private String image;
}
