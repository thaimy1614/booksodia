package com.begin.bg.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = UserControllerTest.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    private User user;
//    private UserRequest request;
//
//    @BeforeEach
//    void initData() {
//        request = UserRequest
//                .builder()
//                .username("thaidq11")
//                .firstName("Thai")
//                .lastName("Duong")
//                .password("123456")
//                .roles(List.of("USER"))
//                .build();
//        user = User.builder()
//                .id(UUID.fromString("b27eb61c-069f-410a-be53-114d4d870a31"))
//                .username("thaidq")
//                .build();
//    }
//
//    @Test
//    public void createUser_validRequest_success() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        String content = objectMapper.writeValueAsString(request);
//        Mockito.when(userService.saveUser(ArgumentMatchers.any())).thenReturn(user);
//
//        //WHEN
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/signup")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(content))
//                //THEN
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//    }
}
