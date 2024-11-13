package com.microservice.identityservice.service;

import com.microservice.identityservice.dto.request.RoleRequest;
import com.microservice.identityservice.dto.request.UpdatePermissionForRoleRequest;
import com.microservice.identityservice.dto.response.RoleResponse;

public interface IRoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updatePermissionForRole(String roleId,UpdatePermissionForRoleRequest roleRequest);
}
