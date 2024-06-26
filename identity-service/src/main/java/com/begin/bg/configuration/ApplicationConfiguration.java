package com.begin.bg.configuration;

import com.begin.bg.entities.Permission;
import com.begin.bg.entities.Role;
import com.begin.bg.entities.User;
import com.begin.bg.enums.UserStatus;
import com.begin.bg.repositories.PermissionRepository;
import com.begin.bg.repositories.RoleRepository;
import com.begin.bg.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class ApplicationConfiguration {

    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            log.info("A");
            if (userRepository.findByEmail("lxlthailxl@gmail.com").isEmpty()) {
                permissionRepository.save(Permission.builder().name("BLOCK_USER").description("Block account").build());
                permissionRepository.save(Permission.builder().name("DELETE_USER").description("Delete account").build());
                log.info("B");
                var allPermissions = permissionRepository.findAll();


                //default roles
                Set<Role> roles = Set.of(
                        roleRepository.save(
                                Role.builder()
                                        .name("ADMIN")
                                        .description("Admin_role")
                                        .permissions(new HashSet<>(allPermissions))
                                        .build()
                        ));
                User user = User
                        .builder()
                        .email("lxlthailxl@gmail.com")
                        .password(passwordEncoder.encode("123"))
                        .roles(roles)
                        .status(UserStatus.ACTIVATED.name())
                        .build();
                userRepository.save(user);
            }

        };
    }
}
