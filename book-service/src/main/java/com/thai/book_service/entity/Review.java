package com.thai.book_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(name = "user_id")
    private String userId;
    private int rating;
    @Column(name = "review_text")
    private String reviewText;
    @Column(name = "review_date")
    private LocalDateTime reviewDate;


}