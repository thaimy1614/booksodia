package com.thai.cart_service.dto.Kafka;

import com.thai.cart_service.model.Book;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InitCartCheckout {
    private String userId;
    private List<Book> books;
}
