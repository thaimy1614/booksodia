package com.thai.cart_service.repository.httpclient;


import com.thai.cart_service.dto.request.client.GetBookRequest;
import com.thai.cart_service.dto.response.BookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "book-service", url = "${app.services.book}")
public interface BookClient {
    @PostMapping(value = "/get-book", produces = MediaType.APPLICATION_JSON_VALUE)
    BookResponse getBook(@RequestBody GetBookRequest request);
}
