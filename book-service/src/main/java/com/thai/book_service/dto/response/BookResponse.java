package com.thai.book_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class BookResponse {
    private String id;
    private String title;
    private String author;
    private String categoryName;
    private int price;
    private int quantity;

    @JsonProperty("publishedDate")
    @JsonSerialize(using = DateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishedDate;
    private double rating;
    private String image;
    private String status;
}
