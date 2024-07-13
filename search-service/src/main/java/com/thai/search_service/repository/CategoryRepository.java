package com.thai.search_service.repository;

import com.thai.search_service.entity.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryRepository extends ElasticsearchRepository<Category, String> {
}
