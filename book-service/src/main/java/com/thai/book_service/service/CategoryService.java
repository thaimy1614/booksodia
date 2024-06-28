package com.thai.book_service.service;

import com.thai.book_service.entity.Book;
import com.thai.book_service.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    Category findById(String id);

    Category save(Category category);

    void delete(String id);

    Category update(Category category);

    List<Book> getAllBooksByCategoryId(String id);
}
