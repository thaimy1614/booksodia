package com.thai.order_service.dto.request.kafka;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Builder
@Data
public class InitCartCheckout {
    private String userId;
    private List<Book> books;
}
