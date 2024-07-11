package com.thai.search_service.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.thai.search_service.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    private final ElasticsearchClient client;

    @GetMapping
    public String search() throws IOException {
        SearchResponse<Book> search = client.search(s -> s
                        .index("dbserver1.public.book"),
                Book.class);

        for (Hit<Book> hit: search.hits().hits()) {
            processProduct(hit.source());
        }
        return search.toString();
    }

    private void processProduct(Book source) {
        log.info(source.getAuthor());
    }
}
