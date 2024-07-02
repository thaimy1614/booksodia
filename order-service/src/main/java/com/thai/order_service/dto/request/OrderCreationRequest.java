package com.thai.order_service.dto.request;

import com.thai.order_service.model.Order_Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreationRequest {
    private String userId;
    private String orderId;
    private List<Order_Book> books;
}
