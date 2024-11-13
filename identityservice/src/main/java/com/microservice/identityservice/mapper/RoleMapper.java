package com.microservice.identityservice.mapper;

import com.microservice.identityservice.dto.request.RoleRequest;
import com.microservice.identityservice.dto.response.PermissionResponse;
import com.microservice.identityservice.dto.response.RoleResponse;
import com.microservice.identityservice.entity.Permission;
import com.microservice.identityservice.entity.Role;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);

    Role toRole(RoleRequest roleRequest);

    Set<PermissionResponse> toRoleResponseSet(Set<Permission> permissions);
}
