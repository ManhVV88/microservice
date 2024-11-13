package com.microservice.identityservice.service;

import com.microservice.identityservice.dto.ListUserResponse;
import com.microservice.identityservice.dto.request.UserRequest;
import com.microservice.identityservice.dto.request.UserUpdatedRequest;
import com.microservice.identityservice.dto.response.UserIdResponse;
import com.microservice.identityservice.dto.response.UserResponse;

import java.util.Map;

public interface IUserService {
    UserResponse createUser(UserRequest userRequest);
    ListUserResponse<UserResponse> getAllUsers(int page, int size);
    UserResponse getUser(String email);
    UserResponse updateUser(UserUpdatedRequest userRequest);
    UserResponse updateRoleUser(String email, String role);
    Map<String,UserIdResponse> getUserId(String email);
}
