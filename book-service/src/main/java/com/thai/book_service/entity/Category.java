package com.thai.book_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @JsonManagedReference
    @OneToMany(mappedBy = "category", targetEntity = Book.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books;

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}
