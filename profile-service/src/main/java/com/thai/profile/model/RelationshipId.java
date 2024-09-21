package com.thai.profile.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipId implements Serializable {
    private User firstUser;
    private User secondUser;
}

