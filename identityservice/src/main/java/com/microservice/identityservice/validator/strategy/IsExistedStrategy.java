package com.microservice.identityservice.validator.strategy;

import com.microservice.identityservice.constant.EntityValidated;
import com.microservice.identityservice.constant.Existed;
import com.microservice.identityservice.exception.ErrorCode;
import com.microservice.identityservice.exception.IdentityException;
import com.microservice.identityservice.repository.PermissionRepository;
import com.microservice.identityservice.repository.RoleRepository;
import com.microservice.identityservice.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class IsExistedStrategy {

    UserRepository userRepository;
    PermissionRepository permissionRepository;
    RoleRepository roleRepository;


    Map<EntityValidated, BiFunction<Existed,String, Boolean>> strategyMap = new EnumMap<>(EntityValidated.class);

    {
        strategyMap.put(EntityValidated.USER,this::isExistUser);
        strategyMap.put(EntityValidated.PERMISSION,this::isExistPermission);
        strategyMap.put(EntityValidated.ROLE,this::isExistRole);
    }

    public Boolean isExist(EntityValidated entityValidated, Existed existedTypeIsPassed, String id){

        var strategy = strategyMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(entityValidated))
                .findFirst()
                .orElseThrow(() -> new IdentityException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        return strategy.getValue().apply(existedTypeIsPassed,id);
    }

    private boolean isExistUser(Existed existed,String email){
        return existed.equals(Existed.EXISTED_IS_PASSED) == userRepository.existsByEmail(email);
    }

    private boolean isExistPermission(Existed existed,String permission){
        return existed.equals(Existed.EXISTED_IS_PASSED) == permissionRepository.existsById(permission);
    }

    private boolean isExistRole(Existed existed,String role){
        return existed.equals(Existed.EXISTED_IS_PASSED) == roleRepository.existsById(role);
    }
}
