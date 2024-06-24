package com.thai.book_service.service;

import com.thai.book_service.entity.Review;
import com.thai.book_service.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;


    @Override
    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Review getReviewById(String id) {
        return reviewRepository.findById(id).orElse(null);
    }

    @Override
    public Review addReview(Review review) {
        return null;
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public void deleteReview(String id) {

    }
}
