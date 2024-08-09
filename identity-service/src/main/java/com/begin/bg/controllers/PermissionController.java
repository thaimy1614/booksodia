package com.begin.bg.controllers;

import com.begin.bg.dto.request.PermissionRequest;
import com.begin.bg.dto.response.PermissionResponse;
import com.begin.bg.entities.ResponseObject;
import com.begin.bg.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.api.prefix}/permission")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    ResponseEntity<ResponseObject> getAllPermissions() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Get all permissions", permissionService.getAll()));
    }

    @PostMapping("/add")
    ResponseEntity<ResponseObject> addPermission(@RequestBody PermissionRequest request) {
        PermissionResponse permissionResponse = permissionService.create(request);
        return permissionResponse != null ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Add new permission successful!", permissionResponse)) :
                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Fail", "Add new permission failed!", null));

    }

    @DeleteMapping("/{permission}")
    ResponseEntity<ResponseObject> deletePermission(@PathVariable(name = "permission") String permission) {
        permissionService.delete(permission);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Delete permission successful!", null));
    }
}
