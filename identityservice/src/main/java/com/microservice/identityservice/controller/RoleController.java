package com.microservice.identityservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.identityservice.dto.request.RoleRequest;
import com.microservice.identityservice.dto.request.UpdatePermissionForRoleRequest;
import com.microservice.identityservice.dto.response.RoleResponse;
import com.microservice.identityservice.service.IRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class RoleController {

    IRoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(
            @RequestBody @Valid RoleRequest roleRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoleResponse>builder()
                        .result(roleService.createRole(roleRequest))
                        .build());
    }
    @PutMapping("/permission/{roleId}")
    public ApiResponse<RoleResponse> setPermission(
            @PathVariable
            @Size(min = 3,max = 7 , message = "INVALID_SIZE")
            @Pattern(regexp = "^[A-Z]+$",message = "INVALID_PATTERN_PERMISSION")
            String roleId,

            @RequestBody
            @Valid
            UpdatePermissionForRoleRequest updatePermissionForRoleRequest
    ){
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updatePermissionForRole(roleId, updatePermissionForRoleRequest))
                .build();
    }
}
