package com.thai.search_service.repository;

import com.thai.search_service.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
    Page<Book> findAllByCategoryAndPriceBetween(String categoryId, int low, int high, Pageable pageable);

    Page<Book> findAllByCategory(String categoryId, Pageable pageable);

    Page<Book> findAllByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String author, String description, Pageable pageable);

    Page<Book> findAllByPriceBetween(int low, int high, Pageable pageable);
}
