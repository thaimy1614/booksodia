package com.thai.book_service.service;

import com.thai.book_service.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviews();
    Review getReviewById(String id);
    Review addReview(Review review);
    Review updateReview(Review review);
    void deleteReview(String id);
}
