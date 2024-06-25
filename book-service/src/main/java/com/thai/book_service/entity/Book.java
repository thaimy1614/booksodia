package com.thai.book_service.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate publishedDate;
    @JsonManagedReference
    @OneToMany(mappedBy = "book", targetEntity = Review.class,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Review> reviews;
    private String description;
    private String image;
}
