package com.begin.bg.services;

import com.begin.bg.dto.request.PermissionRequest;
import com.begin.bg.dto.response.PermissionResponse;
import com.begin.bg.entities.Permission;
import com.begin.bg.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionResponse create(PermissionRequest request){
        Permission permission = Permission
                .builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        permission = permissionRepository.save(permission);
        return PermissionResponse
                .builder()
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }

    public List<PermissionResponse> getAll(){
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permission -> PermissionResponse
                .builder()
                .name(permission.getName())
                .description(permission.getDescription())
                .build()).toList();
    }

    public void delete(String permissionName){
        permissionRepository.deleteById(permissionName);
    }
}
