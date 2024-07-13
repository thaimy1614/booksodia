package com.thai.search_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

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
    private String description;
    private String image;
}
