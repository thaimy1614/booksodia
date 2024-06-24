package com.thai.book_service.service;

import com.thai.book_service.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    List<Book> getBooksByAuthor(String author);
    Book getBookById(String id);
    Book addBook(Book book);
    Book updateBook(Book book);
    void deleteBook(String id);
}
