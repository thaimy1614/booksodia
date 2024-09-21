package com.thai.profile.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class RelationshipId {
    private String bookId;

    private String title;

    private int price;

    private int quantity;

    private String image;
}
