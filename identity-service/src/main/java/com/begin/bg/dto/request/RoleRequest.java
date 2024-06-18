package com.begin.bg.dto.request;

import com.begin.bg.entities.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoleRequest {
    private String name;

    private String description;

    private Set<String> permissions;
}
