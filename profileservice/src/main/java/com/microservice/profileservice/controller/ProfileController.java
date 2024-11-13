package com.microservice.profileservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.profileservice.dto.request.CreateProfileRequest;
import com.microservice.profileservice.dto.response.ProfileResponse;
import com.microservice.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    ProfileService profileService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(@RequestBody CreateProfileRequest profile) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ProfileResponse>builder()
                        .result(profileService.createProfile(profile))
                        .build());
    }

    @GetMapping()
    public ApiResponse<List<ProfileResponse>> getAllProfile() {
        return ApiResponse.<List<ProfileResponse>>builder()
                .result(profileService.getAllProfiles())
                .build();
    }
}
