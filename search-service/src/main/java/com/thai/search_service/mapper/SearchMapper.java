package com.thai.search_service.mapper;

import com.thai.search_service.dto.response.BookResponse;
import com.thai.search_service.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    BookResponse toBookResponse(Book book);
}
