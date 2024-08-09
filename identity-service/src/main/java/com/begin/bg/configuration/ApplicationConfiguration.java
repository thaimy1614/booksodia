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
            if (!roleRepository.existsById("USER")) {
                Set<Permission> userPermission = Set.of(
                        permissionRepository.save(
                                Permission.builder().name("USER_PERMS").description("All user perm").build()
                        )
                );
                roleRepository.save(
                        Role.builder().
                                name("USER")
                                .description("User_role")
                                .permissions(userPermission)
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
                                Permission.builder().name("ADMIN_PERMS").description("All admin perm").build()
                        )
                );

                // default admin roles
                Set<Role> roles = Set.of(
                        roleRepository.save(
                                Role.builder()
                                        .name("ADMIN")
                                        .description("Admin_role")
                                        .permissions(adminPerms)
                                        .build()
                        )
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
