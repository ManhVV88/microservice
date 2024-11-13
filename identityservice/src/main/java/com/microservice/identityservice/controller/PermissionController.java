package com.microservice.identityservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.identityservice.dto.request.PermissionRequest;
import com.microservice.identityservice.dto.response.PermissionResponse;
import com.microservice.identityservice.service.IPermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    IPermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
            @RequestBody @Valid PermissionRequest permissionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PermissionResponse>builder()
                        .result(permissionService.createPermisstion(permissionRequest))
                        .build());
    }
}
