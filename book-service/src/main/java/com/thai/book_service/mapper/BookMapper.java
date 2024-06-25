package com.thai.book_service.mapper;

import com.thai.book_service.dto.response.BookDetailResponse;
import com.thai.book_service.dto.response.BookResponse;
import com.thai.book_service.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookResponse toBookResponse(Book book);
    BookDetailResponse toBookDetailResponse(Book book);
}
