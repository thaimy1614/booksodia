package com.thai.search_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "review")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    @Id
    private String id;
    @JsonBackReference
    @Field(type = FieldType.Nested, includeInParent = true)
    private String book_id;
    private String userId;
    private int rating;
    private String reviewText;
    @Field(type = FieldType.Date, name = "review_date")
    private Date reviewDate;
    @Field(type = FieldType.Date, name = "update_date")
    private Date updateDate;

}
