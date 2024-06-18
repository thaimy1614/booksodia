package com.begin.bg.dto.response;

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
public class RoleResponse {
    private String name;

    private String description;

    private Set<Permission> permissions;
}
