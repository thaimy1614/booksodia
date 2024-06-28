package com.thai.payment_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Cart {
    @Id
    @Column(name = "user_id")
    private String userId;

    @ElementCollection
    @CollectionTable(
            name = "cart_books",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    private List<Cart_Book> books;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDate createdDate;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedDate;
}
