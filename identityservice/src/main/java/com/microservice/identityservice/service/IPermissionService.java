package com.microservice.identityservice.service;

import com.microservice.identityservice.dto.request.PermissionRequest;
import com.microservice.identityservice.dto.response.PermissionResponse;

public interface IPermissionService {
    PermissionResponse createPermisstion(PermissionRequest permissionRequest);
}
