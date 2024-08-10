package com.thai.profile.service.user;

import com.thai.profile.client.AuthenticationClient;
import com.thai.profile.dto.request.BlockUserRequest;
import com.thai.profile.dto.request.ProfileCreationRequest;
import com.thai.profile.dto.request.UnblockUserRequest;
import com.thai.profile.dto.request.UpdateUserDto;
import com.thai.profile.dto.response.user.*;
import com.thai.profile.exception.white.CustomValidationException;
import com.thai.profile.exception.white.UserNotFoundException;
import com.thai.profile.mapper.UserMapper;
import com.thai.profile.model.Role;
import com.thai.profile.model.User;
import com.thai.profile.repository.RoleRepository;
import com.thai.profile.repository.UserRepository;
import com.thai.profile.util.UserValidation;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationClient authenticationClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Cacheable(value = "userCache", condition = "#id != 0", key = "#id")
    public UserResponseDto getUserById(String id) {
        User user;
        if (id.equals("0")) {
            user = User.DEFAULT_USER;
        } else {
            user = userRepository.findByUserId(id).orElseThrow(() -> new UserNotFoundException(id));
        }
        return userMapper.userToUserResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        UserValidation valid = new UserValidation();
        if (!valid.validateEmail(email)) {
            throw new CustomValidationException("Email address is invalid");
        }
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + email));
        return userMapper.userToUserResponseDto(user);
    }

    private ListUserResponseDto createListResponseWithPage(Page<User> page) {
        Pageable nextUserPage = page.nextOrLastPageable();
        int nextPage = nextUserPage.getPageNumber();
        int nextSize = nextUserPage.getPageSize();
        int totalPage = page.getTotalPages();

        List<UserResponseDto> userViews = page.get().map(userMapper::userToUserResponseDto).toList();

        return ListUserResponseDto.builder()
                .nextPage(nextPage)
                .nextSize(nextSize)
                .totalPage(totalPage)
                .totalElement(page.getNumberOfElements())
                .users(userViews)
                .build();
    }

    /**
     * Get all user within the program
     *
     * @param pageable Spring's pageable object for pagination
     * @return pagination info and a list of user
     */
    @Override
    public ListUserResponseDto getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return createListResponseWithPage(userPage);
    }

    @Override
    public ListUserResponseDto getAllUsers(Pageable pageable, String roleName, @Nullable Boolean isActive) {
        final Page<User> userPage;
        if (isActive == null) {
            userPage = userRepository.findAllByRoleName(pageable, roleName);
        } else {
            userPage = userRepository.findAllByRoleNameAndIsActive(pageable, roleName, isActive);
        }

        return createListResponseWithPage(userPage);
    }

    @Override
    @CachePut(value = "userCache", key = "#userId")
    public UserResponseDto updateUser(String userId, UpdateUserDto updateUserDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (updateUserDto.getFullName() != null && !updateUserDto.getFullName().isEmpty()) {
                user.setFullName(updateUserDto.getFullName());
            }
            if (updateUserDto.getImage() != null && !updateUserDto.getImage().isEmpty()) {
                user.setImage(updateUserDto.getImage());
            }
            if (updateUserDto.getTitle() != null && !updateUserDto.getTitle().isEmpty()) {
                user.setTitle(updateUserDto.getTitle());
            }
            if (updateUserDto.getAbout() != null && !updateUserDto.getAbout().isEmpty()) {
                user.setAbout(updateUserDto.getAbout());
            }
            user.setUpdateAt(Timestamp.valueOf(LocalDateTime.now()));
            return userMapper.userToUserResponseDto(userRepository.save(user));
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    @Transactional
    @CachePut(value = "userCache", key = "#userDto.userId")
    public UserResponseDto saveUser(ProfileCreationRequest userDto) {
        log.info(userDto.getUserId());
        User user = userMapper.userRequestDtoToUser(userDto);
        user.setUserId(userDto.getUserId());
        if (userRepository.existsById(user.getUserId())) {
            throw new CustomValidationException("User_ID already exists");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new CustomValidationException("Email already in use");
        }
        final Role role;
        if (userDto.getIsAdmin()) {
            role = roleRepository.findById(Role.ADMIN_VALUE).orElseThrow();
        } else {
            role = roleRepository.findById(Role.USER_VALUE).orElseThrow();
        }
        user.setRoles(Set.of(role));
        return userMapper.userToUserResponseDto(userRepository.save(user));
    }

    @Transactional
    @CacheEvict(value = "userCache", key = "#request.userId")
    public BlockUserResponse blockUser(BlockUserRequest request) {
        final User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));
        user.setIsActive(false);
        userRepository.save(user);

        return new BlockUserResponse(true);
    }

    @Transactional
    public UnblockUserResponse unblockUser(UnblockUserRequest request) {
        final User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        user.setIsActive(true);
        userRepository.save(user);

        return new UnblockUserResponse(true);
    }

    @Override
    public UserCountResponseDto count(Boolean isBlock, @Nullable Role.Value role) {
        long count;
        if (role == null && isBlock == null) {
            count = userRepository.count();
        } else {
            String roleName = "%";
            if (role != null) {
                roleName = role.name();
            }

            boolean isActive = false;
            if (isBlock != null) {
                isActive = isBlock;
            }

            count = userRepository.countByStatusAndRole(isActive, roleName);
        }

        return UserCountResponseDto.builder()
                .count(count)
                .build();
    }
}
