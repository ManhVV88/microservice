package com.microservice.identityservice.mapper;

import com.microservice.identityservice.dto.request.UserRequest;
import com.microservice.identityservice.dto.request.UserUpdatedRequest;
import com.microservice.identityservice.dto.response.RoleResponse;
import com.microservice.identityservice.dto.response.UserIdResponse;
import com.microservice.identityservice.dto.response.UserResponse;
import com.microservice.identityservice.entity.Role;
import com.microservice.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toUser(UserRequest userRequest);

    @Mapping(target = "roles", source = "roles")
    UserResponse toUserResponse(User user);

    Set<RoleResponse> toRoleResponseSet(Set<Role> roles);

    void toUser(UserUpdatedRequest userUpdatedRequest,@MappingTarget User user);

    @Mapping(target = "userId", source = "id")
    UserIdResponse toUserIdResponse(User user);

}
