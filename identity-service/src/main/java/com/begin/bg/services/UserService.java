package com.begin.bg.services;

import com.begin.bg.entities.User;
import com.begin.bg.enums.UserStatus;
import com.begin.bg.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @PostAuthorize("hasPermission(returnObject, 'BLOCK_USER')")
    public Optional<User> findUserById(String id) {
        return userRepository.findById(id);
    }

    @PostAuthorize("returnObject.email == authentication.name")
    public User getMyInfo() {
        var user = SecurityContextHolder.getContext().getAuthentication();
        String name = user.getName();
        return userRepository.findByEmail(name).orElseThrow();
    }

    public Optional<User> findUserByName(String username) {
        return userRepository.findByEmail(username);
    }

    public Boolean userExistsById(String id) {
        return userRepository.existsById(id);
    }

    public User deleteUserById(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setStatus(UserStatus.DELETED.name());
        return user;
    }
}
