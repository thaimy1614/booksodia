package com.thai.search_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Document(indexName = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    private String id;
    @JsonBackReference
    private Book book;
    private String userId;
    private int rating;
    private String reviewText;
    private Date reviewDate;

    private Date updateDate;

}
