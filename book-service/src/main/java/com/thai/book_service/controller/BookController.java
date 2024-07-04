package com.thai.book_service.controller;

import com.thai.book_service.dto.request.BookCreationRequest;
import com.thai.book_service.dto.request.GetBookRequest;
import com.thai.book_service.dto.response.BookDetailResponse;
import com.thai.book_service.dto.response.BookResponse;
import com.thai.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookService bookService;

    @GetMapping()
    List<BookResponse> getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    BookDetailResponse getBookDetail(@PathVariable String id) {
        return bookService.getBookDetail(id);
    }


    @PostMapping("/add")
    BookResponse addBook(@RequestPart BookCreationRequest bookRequest, @RequestParam("file") MultipartFile multipartFile) {
        return bookService.addBook(bookRequest, multipartFile);
    }

    @PutMapping("/{id}")
    BookResponse updateBook(@PathVariable String id, @RequestPart BookCreationRequest bookRequest, @RequestParam("file") MultipartFile multipartFile) {
        return bookService.updateBook(id, bookRequest, multipartFile);
    }

    @DeleteMapping("/{id}")
    void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }

    @PostMapping("/get-book")
    BookResponse getBook(@RequestBody GetBookRequest request){
        return bookService.getBookById(request.getBookId());
    };
}
