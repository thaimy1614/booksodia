package com.thai.profile.service.user;

import fsa.cursus.user_service.client.AuthenticationClient;
import fsa.cursus.user_service.client.ContentClient;
import fsa.cursus.user_service.client.CourseClient;
import fsa.cursus.user_service.dto.CheckUserIds;
import fsa.cursus.user_service.dto.course.CategoryDto;
import fsa.cursus.user_service.dto.mail.UserInfo;
import fsa.cursus.user_service.dto.request.*;
import fsa.cursus.user_service.dto.response.InstructorRegisterResponse;
import fsa.cursus.user_service.dto.response.RegisterResponse;
import fsa.cursus.user_service.dto.response.user.*;
import fsa.cursus.user_service.exception.black.OrderCorrupted;
import fsa.cursus.user_service.exception.white.CustomValidationException;
import fsa.cursus.user_service.exception.white.UserNotFoundException;
import fsa.cursus.user_service.exception.white.UserNotRequest;
import fsa.cursus.user_service.invoker.CourseServiceInvoker;
import fsa.cursus.user_service.mapper.RegisterRequestMapper;
import fsa.cursus.user_service.mapper.UserMapper;
import fsa.cursus.user_service.model.*;
import fsa.cursus.user_service.repository.*;
import fsa.cursus.user_service.util.UserValidation;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EnrollRepository enrollRepository;
    private final InstructorRequestRepository requestRepository;
    private final RegisterRequestMapper registerRequestMapper;
    private final CourseServiceInvoker courseServiceInvoker;
    private final ContentClient contentClient;
    private final AuthenticationClient authenticationClient;
    private final CourseClient courseClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EarningRepository earningRepository;

    @Override
    @Cacheable(value = "userCache", condition = "#id != 0", key = "#id")
    public UserResponseDto getUserById(Long id) {
        User user;
        if (id == 0) {
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

    /**
     * Get all user of the input role {@code roleName}
     *
     * @param pageable Spring's pageable object for pagination
     * @param roleName Role's Value.
     *                 Must be {@link Role#ADMIN_VALUE} or {@link Role#INSTRUCTOR_VALUE} or {@link Role#STUDENT_VALUE}
     * @param isActive User's active status
     * @return pagination info and a list of user of {@code roleName}
     */
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
    public UserResponseDto updateUser(Long userId, UpdateUserDto updateUserDto) {
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
    public UserResponseDto saveUser(UserRequestDto userDto) {
        User user = userMapper.userRequestDtoToUser(userDto);
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
            role = roleRepository.findById(Role.STUDENT_VALUE).orElseThrow();
        }
        user.setRoles(Set.of(role));
        return userMapper.userToUserResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public InstructorRegisterResponse instructorRegistration(Long userId, MultipartFile file, RegisterRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        if (!userRepository.isInstructor(userId)) {
            String cv;
            try {
                cv = contentClient.uploadCv(file);
                if (cv == null || cv.isEmpty()) {
                    throw new CustomValidationException("Cannot upload cv");
                }
            } catch (Exception e) {
                throw new CustomValidationException(e.getMessage());
            }

            courseServiceInvoker.checkCategories(request.getCategoryIds());

            List<TeachingCategoryRequest> list = new ArrayList<>();
            request.getCategoryIds().forEach(id -> list.add(TeachingCategoryRequest.builder().categoryId(id).build()));
            requestRepository.save(InstructorRequest.builder()
                    .description(request.getDescription())
                    .instructorName(user.getFullName())
                    .cv(cv)
                    .userId(userId)
                    .categoryId(list)
                    .build());
        } else {
            throw new CustomValidationException("You don't have permission to start new teaching");
        }
        return new InstructorRegisterResponse(true);
    }

    @Transactional
    @CacheEvict(value = "userCache", key = "#request.userId")
    public BlockUserResponse blockUser(BlockUserRequest request) {
        final User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        // Block user courses if user is an instructor
        if (user.getRoles().contains(Role.INSTRUCTOR)) {
            final List<Long> courseIds = user.getTeachCourses()
                    .stream()
                    .map(Teaching::getCourseId)
                    .toList();
            courseServiceInvoker.blockCourses(courseIds);
        }
        user.setIsActive(false);
        userRepository.save(user);

        return new BlockUserResponse(true);
    }

    @Transactional
    public UnblockUserResponse unblockUser(UnblockUserRequest request) {
        final User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        // Unblock user courses if user is an instructor
        if (user.getRoles().contains(Role.INSTRUCTOR)) {
            final List<Long> courseIds = user.getTeachCourses()
                    .stream()
                    .map(Teaching::getCourseId)
                    .toList();
            courseServiceInvoker.unblockCourses(courseIds);
        }
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

    @Transactional
    public InstructorRegisterResponse approveInstructor(ApproveInstructorRequest request) {
        InstructorRequest instructorRequest = requestRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotRequest(request.getUserId()));

        Optional<User> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException(request.getUserId());
        }
        User currentUser = user.get();
        Set<Role> roles = currentUser.getRoles();
        if (!currentUser.getIsActive() || !(roles.size() == 1 && roles.contains(new Role("student")))) {
            throw new CustomValidationException("Cannot approve instructor because this user doesn't have permission");
        }
        Boolean isUpdated = authenticationClient.approveInstructor(currentUser.getUserId());
        if (Boolean.TRUE.equals(isUpdated)) {
            roles.add(new Role("instructor"));
            currentUser.setRoles(roles);
            List<TeachingCategoryRequest> requests = requestRepository.findById(request.getUserId())
                    .orElseThrow().getCategoryId();
            List<TeachingCategory> categories = registerRequestMapper.toTeachingCategoryList(requests);
            currentUser.setCategoryId(categories);
            userRepository.save(currentUser);
            requestRepository.deleteById(currentUser.getUserId());
            contentClient.deleteCv(UUID.fromString(instructorRequest.getCv()));
            UserInfo userInfo = UserInfo.builder()
                    .topic("AI")
                    .email(currentUser.getEmail())
                    .name(currentUser.getFullName())
                    .build();
            kafkaTemplate.send("user-notification", userInfo);
            return new InstructorRegisterResponse(true);
        }
        return new InstructorRegisterResponse(false);
    }

    public InstructorRegisterResponse rejectInstructor(RejectInstructorRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new UserNotFoundException(request.getUserId())
        );
        InstructorRequest instructorRequest = requestRepository.findById(request.getUserId()).orElseThrow(
                () -> new UserNotRequest(request.getUserId())
        );
        requestRepository.deleteById(request.getUserId());
        contentClient.deleteCv(UUID.fromString(instructorRequest.getCv()));
        UserInfo userInfo = UserInfo.builder()
                .topic("RI")
                .reason(request.getReason())
                .email(user.getEmail())
                .name(user.getFullName())
                .build();
        kafkaTemplate.send("user-notification", userInfo);
        return new InstructorRegisterResponse(true);
    }

    @Transactional
    public List<RegisterResponse> getAllRegisterRequest() {
        List<InstructorRequest> requests = requestRepository.findAll();
        List<RegisterResponse> responses = new ArrayList<>();
        requests.forEach(instructorRequest -> {
            RegisterResponse response = registerRequestMapper.toRegisterResponse(instructorRequest);
            response.setCategoryId(fetchCategory(instructorRequest));
            response.setCv(contentClient.getAccessUrl(UUID.fromString(response.getCv())));
            responses.add(response);
        });
        return responses;
    }

    private List<CategoryDto> fetchCategory(InstructorRequest request) {
        List<Long> categoryIds = request.getCategoryId().stream().map(TeachingCategoryRequest::getCategoryId).toList();
        return courseClient.getListCategory(categoryIds);
    }

    public String getUserFullName(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(userId)
        );
        return user.getFullName();
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public Map<Long, UserInfo> createEnrollmentAndEarning(PaymentCompleteDto paymentCompleteDto) {
        // Check if customer exists
        final User customer = userRepository.findByUserId(paymentCompleteDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(paymentCompleteDto.getUserId()));

        // Validate instructors
        final List<Long> instructorIds = paymentCompleteDto.getCourses()
                .stream()
                .unordered()
                .map(CourseOrder::getInstructorId)
                .distinct()
                .toList();
        final List<User> instructors = userRepository.findAllById(instructorIds);
        if (instructorIds.size() != instructors.size()) {
            throw new UserNotFoundException("Some instructor are missing");
        }

        final Map<Long, UserInfo> userMap = new HashMap<>(instructors.size() + 1);
        for (User instructor : instructors) {
            userMap.put(instructor.getUserId(), UserInfo.createUserInfo(instructor).build());
        }
        userMap.put(customer.getUserId(), UserInfo.createUserInfo(customer).build());

        // Construct new earnings and enrollments
        List<Long> courses = new ArrayList<>(paymentCompleteDto.getCourses().size());
        Earning earning;
        List<Earning> newEarnings = new ArrayList<>(paymentCompleteDto.getCourses().size());
        Enrollment enrollment;
        List<Enrollment> newEnrollments = new ArrayList<>(paymentCompleteDto.getCourses().size());
        for (CourseOrder courseOrder : paymentCompleteDto.getCourses()) {
            // Enrollment
            enrollment = Enrollment.builder()
                    .userId(customer.getUserId())
                    .courseId(courseOrder.getCourseId())
                    .build();
            newEnrollments.add(enrollment);

            // Earning
            if (!userMap.containsKey(courseOrder.getInstructorId())) {
                throw new UserNotFoundException(courseOrder.getInstructorId());
            }
            earning = Earning.builder()
                    .courseId(courseOrder.getCourseId())
                    .orderId(paymentCompleteDto.getOrderId())
                    .userId(courseOrder.getInstructorId())
                    .amount(courseOrder.getCost())
                    .build();
            newEarnings.add(earning);

            // New enrollment coursesId
            courses.add(courseOrder.getCourseId());
        }

        // Save all enrollments and earnings
        try {
            enrollRepository.saveAllAndFlush(newEnrollments);
            earningRepository.saveAllAndFlush(newEarnings);
        } catch (DataIntegrityViolationException e) {
            throw new OrderCorrupted(e);    // relied on DB constraint to check for order integrity problem
        }

        // Increase enrollment count after all steps successes
        courseServiceInvoker.increaseCourseEnrollment(courses);

        return userMap;
    }

    //
    public Boolean checkUserIds(CheckUserIds input) {
        List<Long> userIds = input.getUserIds();
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        for (Long userId : userIds) {
            if (!userRepository.existsById(userId)) {
                return false;
            }
        }
        return true;
    }
}
