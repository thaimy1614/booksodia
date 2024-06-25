package com.thai.book_service.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookCreationRequest {
    private String title;
    private String author;
    private String categoryName;
    private int price;
    private int quantity;
    private LocalDate publishedDate;
}
