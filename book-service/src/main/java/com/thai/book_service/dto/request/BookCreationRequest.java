package com.thai.book_service.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class BookCreationRequest {
    private String title;
    private String author;
    private String categoryName;
    private int price;
    private int quantity;
    private Date publishedDate;
}
