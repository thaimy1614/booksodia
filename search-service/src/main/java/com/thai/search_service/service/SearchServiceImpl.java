package com.thai.search_service.service;

import com.thai.search_service.dto.response.BookResponse;
import com.thai.search_service.entity.Book;
import com.thai.search_service.mapper.SearchMapper;
import com.thai.search_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    private final BookRepository bookRepository;
    private final SearchMapper searchMapper;

    @Override
    public Page<BookResponse> filterBooksByCategoryAndPrice(String category, int minPrice, int maxPrice, Pageable pageable) {
        Page<Book> books = null;
        if (category.equals("0")){
            books = bookRepository.findAllByPriceBetween(minPrice, maxPrice, pageable);
        } else {
            books = bookRepository.findAllByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);
        }
        return books.map(searchMapper::toBookResponse);
    }

    @Override
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(searchMapper::toBookResponse);
    }

    @Override
    public Page<BookResponse> getAllBooksByCategory(String category, Pageable pageable) {
        Page<Book> books = bookRepository.findAllByCategory(category, pageable);
        return books.map(searchMapper::toBookResponse);
    }

    @Override
    public Page<BookResponse> getAllByTitleOrAuthorOrDescriptionContaining(String keyword, Pageable pageable) {
        Page<Book> books = bookRepository.findAllByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, keyword, pageable);
        return books.map(searchMapper::toBookResponse);
    }
}
