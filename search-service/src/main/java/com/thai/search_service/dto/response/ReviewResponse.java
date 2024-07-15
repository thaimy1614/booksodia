package com.thai.search_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ReviewResponse {
    private String id;
    private String book_id;
    private String userId;
    private int rating;
    private String reviewText;
    private Date reviewDate;
    private Date updateDate;
}
