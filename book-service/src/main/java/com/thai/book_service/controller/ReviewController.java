package com.thai.book_service.controller;


import com.thai.book_service.entity.Review;
import com.thai.book_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/{id}")
    public Review addReviewByBookId(@RequestBody Review review, @PathVariable String id) {
        return reviewService.addReview(review, id);
    }

    @GetMapping
    public List<Review> getReviewsByBookId(@RequestParam("bookId") String bookId) {
        return reviewService.getReviewsByBookId(bookId);
    }
}
