package com.begin.bg.configuration;

import com.begin.bg.entities.Permission;
import com.begin.bg.entities.Role;
import com.begin.bg.enums.UserRole;
import com.begin.bg.enums.UserStatus;
import com.begin.bg.entities.User;
import com.begin.bg.repositories.PermissionRepository;
import com.begin.bg.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
public class ApplicationConfiguration {

    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()){
                var permissions = permissionRepository.findAll();
                Set<Role> roles =new HashSet<>();
                roles.add(Role
                        .builder()
                                .name("ADMIN")
                                .description("Admin_role")
                                .permissions(new HashSet<>(permissions))
                        .build());
                User user = User
                        .builder()
                        .username("admin")
                        .password(passwordEncoder.encode("123"))
                        .roles(roles)
                        .status(UserStatus.ACTIVATED.name())
                        .build();
                userRepository.save(user);
            }

        };
    }
}
