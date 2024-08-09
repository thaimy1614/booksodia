package com.begin.bg.controllers;

import com.begin.bg.dto.request.UserRequest;
import com.begin.bg.entities.ResponseObject;
import com.begin.bg.entities.User;
import com.begin.bg.enums.UserStatus;
import com.begin.bg.repositories.RoleRepository;
import com.begin.bg.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${application.api.prefix}/users")
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    //Get detail User
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable String id) {
        Optional<User> foundUser = userService.findUserById(id);
        return foundUser.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "User found", foundUser))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("FAILED", "Cannot find User with id = " + id, null));
    }

    /*
    User getUserByID(@PathVariable UUID id){
        return userService.findById(id).orElseThrow(()->new RuntimeException("ERROR"));
    }
     */

    //Update User or insert User if not found
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateUser(@RequestBody UserRequest newUser, @PathVariable String id) {
        User updatedUser = userService.findUserById(id)
                .map(User -> {
                    User.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    User.setStatus(UserStatus.UNVERIFIED);
                    return userService.saveUser(User);
                }).orElseThrow();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Updated User succesful!", updatedUser));
    }

    @GetMapping("/my-info")
    ResponseEntity<ResponseObject> getMyInfo() {
        User user = userService.getMyInfo();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "GET YOUR INFORMATION SUCCESSFUL!", user));
    }

    //Delete a User
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteUser(@PathVariable String id) {
        Boolean exists = userService.userExistsById(id);
        if (exists) {
            User user = userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Deleted User with id = " + id + " successful!", user));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("FAIL", "User not found", ""));
    }
}
