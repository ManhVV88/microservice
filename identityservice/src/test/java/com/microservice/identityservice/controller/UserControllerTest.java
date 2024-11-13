package com.microservice.identityservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.identityservice.dto.request.UserRequest;
import com.microservice.identityservice.dto.response.RoleResponse;
import com.microservice.identityservice.dto.response.UserResponse;
import com.microservice.identityservice.service.IUserService;
import com.microservice.identityservice.validator.IsExistedEntityIdValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    IUserService userService;

    @SpyBean
    private IsExistedEntityIdValidator isExistedEntityIdValidator;

    private UserRequest userRequest;
    private UserResponse userResponse;



    @BeforeEach
    void setUp() {
        LocalDate dob = LocalDate.of(1990, 1, 1);

        LocalDateTime createDateAndUpdate = LocalDateTime.now();


        userRequest = UserRequest.builder()
                .name("manh")
                .email("manh1@gmail.com")
                .address("vietnam - bac ninh")
                .dateOfBirth(dob)
                .password("123456")
                .build();

        userResponse = UserResponse.builder()
                .name("manh")
                .email("manh1@gmail.com")
                .address("vietnam - bac ninh")
                .dateOfBirth(dob)
                .password("123456")
                .build();

        RoleResponse roleResponse = RoleResponse.builder()
                .name("USER")
                .description("User role")
                .createDate(createDateAndUpdate)
                .updateDate(createDateAndUpdate)
                .build();

        userResponse.setRoles(new HashSet<>());
        userResponse.getRoles().add(roleResponse);
        Mockito.doReturn(true).when(isExistedEntityIdValidator).isValid(Mockito.anyString(), Mockito.any());

    }


    @Test
    void testIsExistedEntityIdValidator() {
        assertTrue(isExistedEntityIdValidator.isValid("any_email", null));
    }
    @Test
    void createUser_validRequest_success() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userRequest);

        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                        .thenReturn(userResponse);



        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("code")
                        .value(1000));
    }

    @Test
    void createUser_invalidSizeEmail_fail() throws Exception {
        userRequest.setEmail("joh");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userRequest);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1012))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Size of email must be between 5 and 36"));
    }
}
