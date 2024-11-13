package com.microservice.identityservice.service;

import com.microservice.identityservice.dto.request.LoginRequest;
import com.microservice.identityservice.dto.request.LogoutRequest;
import com.microservice.identityservice.dto.request.RefreshRequest;
import com.microservice.identityservice.dto.request.ValidateTokenRequest;
import com.microservice.identityservice.dto.response.LoginResponse;
import com.microservice.identityservice.dto.response.ValidateTokenResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuthenticationService {
    LoginResponse refreshToken(LoginRequest loginRequest);
    void logout(LogoutRequest logoutRequest) throws JOSEException, ParseException;
    ValidateTokenResponse validateToken(ValidateTokenRequest tokenRequest) throws JOSEException, ParseException;
    LoginResponse refreshToken(RefreshRequest refreshRequest) throws ParseException, JOSEException;
    LoginResponse loginGoogle(String code);

}
