package com.microservice.profileservice.mapper;

import com.microservice.profileservice.dto.request.CreateProfileRequest;
import com.microservice.profileservice.dto.response.ProfileResponse;
import com.microservice.profileservice.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "createDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    UserProfile toUserProfile(CreateProfileRequest createProfileRequest);

    ProfileResponse toProfileResponse(UserProfile userProfile);
}
