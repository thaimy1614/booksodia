package com.thai.payment_service.dto.response;

import com.thai.payment_service.model.CartItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReadCartResponse {
    private List<CartItem> cart;
}
