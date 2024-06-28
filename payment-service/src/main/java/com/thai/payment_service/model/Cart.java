package com.thai.payment_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
