package com.thai.order_service.dto.request.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitCartCheckout {
    private String userId;
    private String orderId;
    private List<Book> books;
}

