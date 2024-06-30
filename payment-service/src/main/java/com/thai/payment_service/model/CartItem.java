package com.thai.payment_service.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "cart")
@IdClass(CartItemKey.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItem {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "book_id")
    private String bookId;

    private Long price;

    private int quantity;

    @CreationTimestamp
    @JsonProperty("createAt")
    @JsonSerialize(using = DateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createAt;
}
