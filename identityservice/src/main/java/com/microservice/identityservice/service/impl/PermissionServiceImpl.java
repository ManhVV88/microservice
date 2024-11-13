package com.microservice.identityservice.service.impl;

import com.microservice.identityservice.dto.request.PermissionRequest;
import com.microservice.identityservice.dto.response.PermissionResponse;
import com.microservice.identityservice.entity.Permission;
import com.microservice.identityservice.mapper.PermissionMapper;
import com.microservice.identityservice.repository.PermissionRepository;
import com.microservice.identityservice.service.IPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermissionServiceImpl implements IPermissionService {
    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermisstion(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }
}
