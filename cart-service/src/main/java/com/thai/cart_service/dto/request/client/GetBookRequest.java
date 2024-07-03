package com.thai.cart_service.dto.request.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetBookRequest {
    String bookId;
}
