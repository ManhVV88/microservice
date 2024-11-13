package com.microservice.identityservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.identityservice.dto.request.LoginRequest;
import com.microservice.identityservice.dto.request.LogoutRequest;
import com.microservice.identityservice.dto.request.RefreshRequest;
import com.microservice.identityservice.dto.request.ValidateTokenRequest;
import com.microservice.identityservice.dto.response.LoginResponse;
import com.microservice.identityservice.dto.response.ValidateTokenResponse;
import com.microservice.identityservice.service.IAuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {

    IAuthenticationService authenticationService;

    @PostMapping(value = "/login")
    ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ApiResponse.<LoginResponse>builder()
                .result(authenticationService.refreshToken(loginRequest))
                .build();
    }

    @PostMapping(value = "/logout")
    ApiResponse<Void> login(@RequestBody LogoutRequest logoutRequest)
            throws ParseException, JOSEException {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<Void>builder()
                .message("Logout successful")
                .build();
    }


    @PostMapping(value = "/refresh")
    ApiResponse<LoginResponse> login(@RequestBody RefreshRequest refreshRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<LoginResponse>builder()
                .result(authenticationService.refreshToken(refreshRequest))
                .build();
    }

    @PostMapping(value = "/validation")
    ApiResponse<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest validateTokenRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<ValidateTokenResponse>builder()
                .result(authenticationService.validateToken(validateTokenRequest))
                .build();
    }

    @PostMapping(value = "/google/authentication")
    ApiResponse<LoginResponse> googleAuthentication(@RequestParam String code){
        return  ApiResponse.<LoginResponse>builder()
                .result(authenticationService.loginGoogle(code))
                .build();
    }
}
