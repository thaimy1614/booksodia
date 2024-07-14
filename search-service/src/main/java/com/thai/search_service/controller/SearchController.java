package com.thai.search_service.controller;

import com.thai.search_service.dto.ResponseObject;
import com.thai.search_service.dto.response.BookResponse;
import com.thai.search_service.entity.Book;
import com.thai.search_service.mapper.SearchMapper;
import com.thai.search_service.repository.BookRepository;
import com.thai.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    private final BookRepository bookRepository;
    private final SearchService searchService;
    private final SearchMapper searchMapper;

    @GetMapping
    ResponseObject<Page<BookResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Sort sort = Sort.by(Sort.Direction.DESC, "price");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Book> books = bookRepository.findAllByPriceBetween(5000.0, 30000.0, pageable);
        return new ResponseObject<>(HttpStatus.OK, "Get all books successfully", books.map(searchMapper::toBookResponse));
    }

    @GetMapping("/query")
    public String searchByTitle(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam("title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) throws IOException {
        return null;
    }

    @GetMapping("/category/{category}")
    BookResponse getAllByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return searchMapper.toBookResponse(bookRepository.findById(category).get());
    }

}
