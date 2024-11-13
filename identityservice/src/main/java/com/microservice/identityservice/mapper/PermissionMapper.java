package com.microservice.identityservice.mapper;

import com.microservice.identityservice.dto.request.PermissionRequest;
import com.microservice.identityservice.dto.response.PermissionResponse;
import com.microservice.identityservice.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toPermissionResponse(Permission permission);

    Permission toPermission(PermissionRequest permissionRequest);
}
