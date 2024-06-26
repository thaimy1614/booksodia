package com.begin.bg.controllers;

import com.begin.bg.dto.request.RoleRequest;
import com.begin.bg.dto.response.RoleResponse;
import com.begin.bg.entities.ResponseObject;
import com.begin.bg.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    ResponseEntity<ResponseObject> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Get all roles", roleService.getAllRoles()));
    }

    @PostMapping("/add")
    ResponseEntity<ResponseObject> addRole(@RequestBody RoleRequest request) {
        RoleResponse roleResponse = roleService.create(request);
        return roleResponse != null ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Add new role successful!", roleResponse)) :
                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Fail", "Add new role failed!", null));

    }

    @DeleteMapping("/{role}")
    ResponseEntity<ResponseObject> deletePermission(@PathVariable(name = "role") String role) {
        roleService.deleteRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Delete role successful!", null));
    }
}
