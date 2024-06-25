package com.thai.book_service.service;

import com.thai.book_service.dto.response.BookResponse;
import com.thai.book_service.entity.Book;

import java.util.List;

public interface BookService {
    List<BookResponse> getAllBooks();
    List<BookResponse> getBooksByAuthor(String author);
    BookResponse getBookById(String id);
    BookResponse addBook(Book book);
    BookResponse updateBook(Book book);
    void deleteBook(String id);
}
