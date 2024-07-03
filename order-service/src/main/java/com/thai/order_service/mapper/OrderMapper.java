package com.thai.order_service.mapper;

import com.thai.order_service.dto.request.kafka.Book;
import com.thai.order_service.model.Order_Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    List<Order_Book> toOrderBook(List<Book> book);
}
