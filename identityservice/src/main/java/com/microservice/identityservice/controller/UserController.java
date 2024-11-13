package com.microservice.identityservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.identityservice.dto.ListUserResponse;
import com.microservice.identityservice.dto.request.UpdateRoleUserRequest;
import com.microservice.identityservice.dto.request.UserRequest;
import com.microservice.identityservice.dto.request.UserUpdatedRequest;
import com.microservice.identityservice.dto.response.UserIdResponse;
import com.microservice.identityservice.dto.response.UserResponse;
import com.microservice.identityservice.service.IUserService;
import com.microservice.identityservice.validator.IsExistedEntityIdConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Validated
public class UserController {

    IUserService userService;

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .result(userService.createUser(userRequest))
                        .build());
    }

    @GetMapping
    public ApiResponse<ListUserResponse<UserResponse>> getAllUsers(
            @Valid @Min(value = 1 , message = "INVALID_PAGE_MIN") @RequestParam(defaultValue = "1") int page,
            @Valid @Min(value = 1 , message = "INVALID_PAGE_MIN") @RequestParam(defaultValue = "10") int size)
    {
        return ApiResponse.<ListUserResponse<UserResponse>>builder()
                .result(userService.getAllUsers(page-1, size))
                .build();
    }

    @GetMapping("/info")
    public ApiResponse<UserResponse> getUserByEmail(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(
                        SecurityContextHolder.getContext().getAuthentication().getName()))
                .build();
    }

    @PutMapping
    public ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserUpdatedRequest userUpdatedRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userUpdatedRequest))
                .build();
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN') or (authentication.name == #email)")
    public ApiResponse<UserResponse> getUserByEmail(
            @PathVariable
            @Pattern(regexp = "^\\S+@\\S+\\.\\S+$",message = "INVALID_EMAIL_FORMAT")
            String email
    ) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(email))
                .build();
    }

    @GetMapping("/valid-email/{email}")
    public ApiResponse<Boolean> isExistEmail(
            @PathVariable
            @IsExistedEntityIdConstraint(message = "INVALID_EMAIL_NOT_EXISTED")
            String email) {
        return ApiResponse.<Boolean>builder()
                .result(true)
                .build();
    }
    @PutMapping("/role/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponse> updateUserRole(
            @PathVariable
            @Pattern(regexp = "^\\S+@\\S+\\.\\S+$",message = "INVALID_EMAIL_FORMAT")
            String email,
            @RequestBody @Valid UpdateRoleUserRequest updateRoleUserRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateRoleUser(email,updateRoleUserRequest.getRole()))
                .build();
    }

    @GetMapping("/get_id/{email}")
    public ApiResponse<Map<String,UserIdResponse>> getUserId(@PathVariable("email") String email) {
        return ApiResponse.<Map<String,UserIdResponse>>builder()
                .result(userService.getUserId(email))
                .build();
    }
}
