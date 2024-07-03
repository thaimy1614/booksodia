package com.thai.cart_service.mapper;

import com.thai.cart_service.model.Book;
import com.thai.cart_service.model.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {
    List<Book> toBook(List<CartItem> cartItem);
}
