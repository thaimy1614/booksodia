package com.thai.payment_service.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DeleteItemRequest {
    private String userId;
    private List<String> bookId;
}
