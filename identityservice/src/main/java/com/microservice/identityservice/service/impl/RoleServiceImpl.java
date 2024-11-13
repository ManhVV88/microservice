package com.microservice.identityservice.service.impl;

import com.microservice.identityservice.dto.request.RoleRequest;
import com.microservice.identityservice.dto.request.UpdatePermissionForRoleRequest;
import com.microservice.identityservice.dto.response.RoleResponse;
import com.microservice.identityservice.entity.Permission;
import com.microservice.identityservice.entity.Role;
import com.microservice.identityservice.exception.ErrorCode;
import com.microservice.identityservice.exception.IdentityException;
import com.microservice.identityservice.mapper.RoleMapper;
import com.microservice.identityservice.repository.PermissionRepository;
import com.microservice.identityservice.repository.RoleRepository;
import com.microservice.identityservice.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse updatePermissionForRole(String roleId,UpdatePermissionForRoleRequest roleRequest) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IdentityException(ErrorCode.INVALID_ROLE_NOT_EXIST));
        Permission permissionRequest = permissionRepository.findById(roleRequest.getPermissionName())
                .orElseThrow(() -> new IdentityException(ErrorCode.INVALID_PERMISSION_NOT_EXIST));
        if("ADD".equals(roleRequest.getUpdateType())) {
            role.getPermissions().add(permissionRequest);
        } else {
            role.getPermissions().removeIf(permission -> permission.equals(permissionRequest));
        }
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }
}
