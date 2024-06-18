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
import java.util.UUID;

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

    @PostAuthorize("returnObject.get().username == authentication.name || hasRole('ADMIN')")
    public Optional<User> findUserById(UUID id) {
        return userRepository.findById(id);
    }
    @PostAuthorize("returnObject.username == authentication.name")
    public User getMyInfo(){
        var user = SecurityContextHolder.getContext().getAuthentication();
        String name = user.getName();
        return userRepository.findByUsername(name).get();
    }

    public Optional<User> findUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public Boolean userExistsById(UUID id) {
        return userRepository.existsById(id);
    }

    public User deleteUserById(UUID id) {
        User user = userRepository.findById(id).get();
        user.setStatus(UserStatus.DELETED.name());
        return user;
    }
}
