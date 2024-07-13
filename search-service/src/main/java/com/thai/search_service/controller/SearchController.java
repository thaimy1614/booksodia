package com.thai.search_service.controller;

import com.thai.search_service.entity.Book;
import com.thai.search_service.repository.BookRepository;
import com.thai.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    private final BookRepository bookRepository;
    private final SearchService searchService;

    @GetMapping
    public String getAll() {
        List<Book> bookList = bookRepository.findAll();
        return bookList.toString();
    }

    @GetMapping("/query")
    public String searchByTitle(@RequestParam String title, @RequestParam int price) throws IOException {
        return searchService.filterBooksByCategoryAndPrice(title, price).toString();
    }

}
