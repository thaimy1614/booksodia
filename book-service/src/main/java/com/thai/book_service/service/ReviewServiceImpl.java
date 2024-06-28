package com.thai.book_service.service;

import com.thai.book_service.entity.Book;
import com.thai.book_service.entity.Review;
import com.thai.book_service.repository.BookRepository;
import com.thai.book_service.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Override
    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsByBookId(String id) {
        Book book = bookRepository.findById(id).orElseThrow();
        return book.getReviews();
    }

    @Override
    public Review addReview(Review review, String id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Book book = bookRepository.findById(id).orElseThrow();
        review.setBook(book);
        review.setUserId(userId);
        List<Review> reviews = book.getReviews();
        reviews.add(review);
        book.setReviews(reviews);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public void deleteReview(String id) {

    }
}
