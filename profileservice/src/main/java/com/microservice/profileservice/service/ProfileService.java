package com.microservice.profileservice.service;

import com.microservice.profileservice.dto.request.CreateProfileRequest;
import com.microservice.profileservice.dto.response.ProfileResponse;

import java.util.List;

public interface ProfileService {
    ProfileResponse createProfile(CreateProfileRequest request);
    List<ProfileResponse> getAllProfiles();
}
