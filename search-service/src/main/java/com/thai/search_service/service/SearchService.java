package com.thai.search_service.service;

import com.thai.search_service.dto.response.BookResponse;
import com.thai.search_service.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    Page<BookResponse> filterBooksByCategoryAndPrice(String category, int minPrice, int maxPrice, Pageable pageable);
    Page<BookResponse> getAllBooks(Pageable pageable);
    Page<BookResponse> getAllBooksByCategory(String category, Pageable pageable);
    // search full-text
    Page<BookResponse> getAllByTitleOrAuthorOrDescriptionContaining(String keyword, Pageable pageable);
}
