package com.thai.search_service.service;

import com.thai.search_service.entity.Book;

import java.io.IOException;
import java.util.List;

public interface SearchService {
    List<Book> filterBooksByCategoryAndPrice(String category, int price);
}
