package com.begin.bg.services;

import com.begin.bg.dto.request.RoleRequest;
import com.begin.bg.dto.response.RoleResponse;
import com.begin.bg.entities.Role;
import com.begin.bg.repositories.PermissionRepository;
import com.begin.bg.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request) {
        var list = permissionRepository.findAllById(request.getPermissions());
        Role role = Role
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .permissions(new HashSet<>(list))
                .build();
        roleRepository.save(role);
        return RoleResponse.builder()
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getPermissions())
                .build();

    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(role -> RoleResponse
                .builder()
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getPermissions())
                .build()).toList();
    }

    public void deleteRole(String name) {
        roleRepository.deleteById(name);
    }
}
