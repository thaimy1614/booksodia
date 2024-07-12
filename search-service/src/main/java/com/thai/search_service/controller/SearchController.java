package com.thai.search_service.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.thai.search_service.entity.Book;
import com.thai.search_service.repository.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    private final ElasticsearchClient client;
    private final Repository repository;

    @GetMapping
    public String search() throws IOException {
        List<Book> bookList = repository.findAll();
        return bookList.toString();
    }

    private void processProduct(Book source) {
        log.info(source.getAuthor());
    }
}
