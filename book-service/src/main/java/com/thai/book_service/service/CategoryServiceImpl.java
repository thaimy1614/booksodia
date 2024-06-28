package com.thai.book_service.service;

import com.thai.book_service.entity.Book;
import com.thai.book_service.entity.Category;
import com.thai.book_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(String id) {
        return null;
    }

    @Override
    public Category save(Category category) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public List<Book> getAllBooksByCategoryId(String id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        return category.getBooks();
    }
}
