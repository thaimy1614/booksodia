package com.thai.cart_service.dto.request.client;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetBookRequest {
    String bookId;
}
