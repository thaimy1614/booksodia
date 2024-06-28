package com.thai.payment_service.dto.request;

import com.thai.payment_service.model.Cart_Book;
import com.thai.payment_service.model.Order_Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.print.Book;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartCreationRequest {
    private String userId;
    private Cart_Book book;
}
