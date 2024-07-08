package com.thai.order_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "user_id")
    private String userId;
    @Column(name = "total_amount")
    private int totalAmount;
    @Column(name = "total_quantity")
    private int totalQuantity;

    @ElementCollection
    @CollectionTable(
            name = "order_books",
            joinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order_Book> books;

    @JsonIgnore
    private String address;
    @JsonIgnore
    private String phone;
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdDate;
    @JsonIgnore
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedDate;

    public enum Status {
        PENDING,
        DONE,
        REJECTED,
    }
}
