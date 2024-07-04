package com.begin.bg.controllers;

import com.begin.bg.dto.request.UserRequest;
import com.begin.bg.entities.User;
import com.begin.bg.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

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
