package com.thai.search_service.controller;

import com.thai.search_service.dto.ResponseObject;
import com.thai.search_service.dto.response.BookResponse;
import com.thai.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    ResponseObject<Page<BookResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponse> books = searchService.getAllBooks(pageable);
        return new ResponseObject<>(HttpStatus.OK, "Get all books successfully", books);
    }

    @GetMapping("/filter")
    ResponseObject<Page<BookResponse>> filter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(name = "min", defaultValue = "0") int minPrice,
            @RequestParam(name = "max", defaultValue = "1000000000") int maxPrice,
            @RequestParam(defaultValue = "0") String cid,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        String sortField = sortField(sortBy);
        Pageable pageable = createPageable(page, size, sortField, direction);
        Page<BookResponse> response = searchService.filterBooksByCategoryAndPrice(cid, minPrice, maxPrice, pageable);
        return new ResponseObject<>(HttpStatus.OK, "Get all books successfully", response);
    }

    @GetMapping("/query")
    ResponseObject<Page<BookResponse>> searchByTitleOrAuthorOrDescriptionContaining(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam("title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        String sortField = sortField(sortBy);
        Pageable pageable = createPageable(page, size, sortField, direction);
        Page<BookResponse> response = searchService.getAllByTitleOrAuthorOrDescriptionContaining(keyword, pageable);
        return new ResponseObject<>(HttpStatus.OK, "Search books by keyword successfully", response);
    }

    @GetMapping("/category/{category}")
    ResponseObject<Page<BookResponse>> getAllByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        String sortField = sortField(sortBy);
        Pageable pageable = createPageable(page, size, sortField, direction);
        Page<BookResponse> response = searchService.getAllBooksByCategory(category, pageable);
        return new ResponseObject<>(HttpStatus.OK, "Search books by category successfully", response);
    }

    String sortField(String sortBy) {
        if (sortBy.equals("title")) {
            return "title.keyword";
        }
        return sortBy;
    }

    Pageable createPageable(int page, int size, String sortBy, String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction.toUpperCase()), sortBy);
        return PageRequest.of(page, size, sort);
    }

}
