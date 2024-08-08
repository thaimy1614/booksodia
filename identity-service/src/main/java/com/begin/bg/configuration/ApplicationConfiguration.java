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
            // Default student role
            if (!roleRepository.existsById("STUDENT")) {
                Set<Permission> studentPerms = Set.of(
                        permissionRepository.save(
                                Permission.builder().name("STUDENT_PERMS").description("All student perm").build()
                        )
                );
                roleRepository.save(
                        Role.builder().
                                name("STUDENT")
                                .description("Student_role")
                                .permissions(studentPerms)
                                .build()
                );
            }

            // Default instructor role
            if (!roleRepository.existsById("INSTRUCTOR")) {
                Set<Permission> instructorPerms = Set.of(
                        permissionRepository.save(
                                Permission.builder().name("INSTRUCTOR_PERMS").description("All instructor perm").build()
                        )
                );
                roleRepository.save(
                        Role.builder().
                                name("INSTRUCTOR")
                                .description("Instructor_role")
                                .permissions(instructorPerms)
                                .build()
                );
            }

            // Default admin account
            if (userRepository.findByEmail("admin@mail").isEmpty()) {
                Set<Permission> adminPerms = Set.of(
                        permissionRepository.save(
                                Permission.builder().name("BLOCK_USER").description("Block account").build()
                        ),
                        permissionRepository.save(
                                Permission.builder().name("DELETE_USER").description("Delete account").build()
                        ),
                        permissionRepository.save(
                                Permission.builder().name("ACCEPT_COURSE").description("Accept course").build()
                        ),
                        permissionRepository.save(
                                Permission.builder().name("ACCEPT_INSTRUCTOR").description("Accept instructor").build()
                        ),
                        permissionRepository.save(
                                Permission.builder().name("ADMIN_PERMS").description("All admin perm").build()
                        )
                );

                // default admin roles
                Set<Role> roles = Set.of(
                        Role.builder()
                                .name("ADMIN")
                                .description("Admin_role")
                                .permissions(adminPerms)
                                .build()
                );

                //admin account
                User account = User
                        .builder()
                        .email("admin@mail")
                        .password(passwordEncoder.encode("123"))
                        .roles(roles)
                        .status(UserStatus.ACTIVATED)
                        .build();
                userRepository.save(account);
            }
        };
    }
}
