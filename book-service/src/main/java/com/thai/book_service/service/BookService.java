package com.thai.book_service.service;

import com.thai.book_service.dto.request.BookCreationRequest;
import com.thai.book_service.dto.response.BookDetailResponse;
import com.thai.book_service.dto.response.BookResponse;
import com.thai.book_service.entity.Book;

import java.util.List;

public interface BookService {
    List<BookResponse> getAllBooks();
    List<BookResponse> getBooksByAuthor(String author);
    BookResponse getBookById(String id);
    BookResponse addBook(BookCreationRequest request);
    BookResponse updateBook(String id, BookCreationRequest request);
    BookDetailResponse getBookDetail(String id);
    void deleteBook(String id);
}
