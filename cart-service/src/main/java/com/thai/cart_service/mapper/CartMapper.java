package com.thai.cart_service.mapper;

import com.thai.cart_service.dto.kafka.Book;
import com.thai.cart_service.dto.response.BookResponse;
import com.thai.cart_service.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface CartMapper {
    CartItem toCartItem(BookResponse bookResponse);
    List<Book> toBookList(List<CartItem> cartItems);
}
