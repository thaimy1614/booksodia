package com.thai.cart_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookResponse {
    private String id;
    private String title;
    private String author;
    private String categoryName;
    private int price;
    private int quantity;
    private LocalDate publishedDate;
    private double rating;
    private String image;
    private String status;
}
