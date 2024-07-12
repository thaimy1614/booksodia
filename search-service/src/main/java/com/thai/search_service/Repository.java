package com.thai.search_service;

import com.thai.search_service.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface Repository extends ElasticsearchRepository<Book, String> {

}
