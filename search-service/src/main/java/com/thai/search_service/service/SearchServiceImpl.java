package com.thai.search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.thai.search_service.entity.Book;
import com.thai.search_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    private final BookRepository bookRepository;

    @Override
    public List<Book> filterBooksByCategoryAndPrice(String searchTerm, int price) {
        List<Book> books = bookRepository.searchBooksByTitleAndPriceLessThan(searchTerm, price);

        return books;
    }
}
