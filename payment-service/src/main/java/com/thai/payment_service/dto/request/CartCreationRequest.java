package com.thai.payment_service.dto.request;

import com.thai.payment_service.model.Cart_Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartCreationRequest {
    private String userId;
    private Cart_Book book;
}
