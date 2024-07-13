package com.thai.search_service.repository;

import com.thai.search_service.entity.Book;
import jakarta.annotation.Nonnull;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
    @Nonnull
    List<Book> findAll();

    List<Book> searchBooksByTitle(String title);

    List<Book> searchBooksByTitleAndPriceLessThan(String author, int price);
}
