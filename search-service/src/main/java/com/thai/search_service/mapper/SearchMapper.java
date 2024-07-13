package com.thai.search_service.mapper;

import com.thai.search_service.dto.response.BookDetailResponse;
import com.thai.search_service.dto.response.BookResponse;
import com.thai.search_service.dto.response.CategoryResponse;
import com.thai.search_service.dto.response.ReviewResponse;
import com.thai.search_service.entity.Book;
import com.thai.search_service.entity.Category;
import com.thai.search_service.entity.Review;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    List<BookResponse> toBookResponse(List<Book> books);
    BookDetailResponse toBookDetailResponse(Book book);

    List<ReviewResponse> toReviewResponse(List<Review> reviews);

    List<CategoryResponse> toCategoryResponse(List<Category> categories);
}
