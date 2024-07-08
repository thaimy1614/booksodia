package com.thai.book_service.dto.response;

import com.thai.book_service.entity.Review;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class BookDetailResponse {
    private String id;
    private String title;
    private String author;
    private String categoryName;
    private int price;
    private int quantity;
    private Date publishedDate;
    private double rating;
    private String description;
    private String imageUrl;
    private List<Review> reviews;
    private String status;
}
