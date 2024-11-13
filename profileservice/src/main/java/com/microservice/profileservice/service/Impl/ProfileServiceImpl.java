package com.microservice.profileservice.service.Impl;

import com.microservice.profileservice.dto.request.CreateProfileRequest;
import com.microservice.profileservice.dto.response.ProfileResponse;
import com.microservice.profileservice.entity.UserProfile;
import com.microservice.profileservice.exception.ErrorCode;
import com.microservice.profileservice.exception.ProfileException;
import com.microservice.profileservice.mapper.ProfileMapper;
import com.microservice.profileservice.repository.ProfileRepository;
import com.microservice.profileservice.repository.httpClient.IdentityClient;
import com.microservice.profileservice.service.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileServiceImpl implements ProfileService {

    ProfileRepository profileRepository;
    ProfileMapper profileMapper;
    IdentityClient identityClient;


    @Override
    public ProfileResponse createProfile(CreateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        if(!identityClient.isExistEmail(email).getResult())
            throw new ProfileException(ErrorCode.INVALID_EMAIL_NOT_EXISTED);
        UserProfile userProfile = profileMapper.toUserProfile(request);
        userProfile.setEmail(email);
        profileRepository.save(userProfile);
        return profileMapper.toProfileResponse(userProfile);
    }

    @Override
    public List<ProfileResponse> getAllProfiles() {
        return profileRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .stream().map(profileMapper::toProfileResponse)
                .toList();
    }
}
