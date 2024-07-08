package com.thai.book_service.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String author;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private int price;
    private int quantity;
    @Column(name = "published_date")
    @JsonProperty("publishedDate")
    @JsonSerialize(using = DateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishedDate;
    @JsonManagedReference
    @OneToMany(mappedBy = "book", targetEntity = Review.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews;
    private String description;
    private String image;
    private String status;

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", publishedDate=" + publishedDate +
                ", category=" + category.getName() + // Avoid full object reference
                '}';
    }
}
