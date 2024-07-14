package com.thai.search_service.repository;

import com.thai.search_service.entity.Review;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReviewRepository extends ElasticsearchRepository<Review, String> {
}
