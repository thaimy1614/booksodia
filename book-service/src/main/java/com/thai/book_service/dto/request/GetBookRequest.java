package com.thai.book_service.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetBookRequest {
    String bookId;
}
