package com.thai.profile.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDate;

@Node("user_profile")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator.class)
    private String id;
    @Property("user_id")
    private String userId;
    @Property("first_name")
    private String firstName;
    @Property("last_name")
    private String lastName;
    @Property("dob")
    private LocalDate dob;
    @Property("city")
    private String city;
    @Property("avatar")
    private String avatar;
}
