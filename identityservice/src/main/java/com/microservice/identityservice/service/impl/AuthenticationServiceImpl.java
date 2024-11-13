package com.microservice.identityservice.service.impl;

import com.microservice.identityservice.constant.CRole;
import com.microservice.identityservice.dto.request.*;
import com.microservice.identityservice.dto.response.LoginResponse;
import com.microservice.identityservice.dto.response.ValidateTokenResponse;
import com.microservice.identityservice.entity.LogoutToken;
import com.microservice.identityservice.entity.Role;
import com.microservice.identityservice.entity.User;
import com.microservice.identityservice.exception.ErrorCode;
import com.microservice.identityservice.exception.IdentityException;
import com.microservice.identityservice.repository.LogoutTokenRepository;
import com.microservice.identityservice.repository.RoleRepository;
import com.microservice.identityservice.repository.UserRepository;
import com.microservice.identityservice.repository.httpClient.GoogleIdenityClient;
import com.microservice.identityservice.repository.httpClient.GoogleInfoUserClient;
import com.microservice.identityservice.service.IAuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${google.identity.client-id}")
    String GOOGLE_IDENTITY_CLIENT_ID;

    @NonFinal
    @Value("${google.identity.client-secret}")
    String GOOGLE_IDENTITY_CLIENT_SECRET;

    @NonFinal
    @Value("${google.identity.redirect-uri}")
    String GOOGLE_IDENTITY_REDIRECT_URI;

    @NonFinal
    @Value("${google.identity.grand-type}")
    String GOOGLE_IDENTITY_GRAND_TYPE;

    UserRepository userRepository;
    LogoutTokenRepository logoutTokenRepository;
    GoogleIdenityClient googleIdenityClient;
    GoogleInfoUserClient googleInfoUserClient;
    RoleRepository roleRepository;

    @Override
    public LoginResponse refreshToken(LoginRequest loginRequest) {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(
                        () ->new IdentityException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        log.info("Authenticated: {}", authenticated);
        if (!authenticated) {
            throw new IdentityException(ErrorCode.INVALID_PASSWORD);
        }

        var token = generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .authenticated(authenticated)
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws JOSEException, ParseException{
        try {
            var signedJWT = verifyToken(logoutRequest.getToken(),true);
            logoutTokenRepository.save(
                    LogoutToken.builder()
                            .tokenId(signedJWT.getJWTClaimsSet().getJWTID())
                            .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                            .build()
            );
        } catch (IdentityException e){
            log.info("token already expired");
        }
    }

    @Override
    public ValidateTokenResponse validateToken(ValidateTokenRequest tokenRequest)
    throws JOSEException, ParseException {
        boolean isValid = true;
        var token = tokenRequest.getToken();

        try {
            verifyToken(token,false);
        } catch (IdentityException e) {
            isValid = false;
        }

        return ValidateTokenResponse.builder()
                .validToken(isValid)
                .build();
    }

    @Override
    public LoginResponse refreshToken(RefreshRequest refreshRequest) throws ParseException, JOSEException {

        SignedJWT signedJWT = verifyToken(refreshRequest.getToken(),true);

        logoutTokenRepository.save(LogoutToken.builder()
                        .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                        .tokenId(signedJWT.getJWTClaimsSet().getJWTID())
                        .build());

        return LoginResponse.builder()
                .token(generateToken(
                        userRepository.findByEmail(signedJWT.getJWTClaimsSet().getSubject())
                                .orElseThrow(()-> new IdentityException(ErrorCode.USER_NOT_EXISTED))
                ))
                .authenticated(true)
                .build();
    }

    @Override
    public LoginResponse loginGoogle(String code) {
        var response = googleIdenityClient.getGoogleToken(GoogleTokenRequest.builder()
                        .code(code)
                        .clientId(GOOGLE_IDENTITY_CLIENT_ID)
                        .redirectUri(GOOGLE_IDENTITY_REDIRECT_URI)
                        .clientSecret(GOOGLE_IDENTITY_CLIENT_SECRET)
                        .grantType(GOOGLE_IDENTITY_GRAND_TYPE)
                .build());
        log.info("TOKEN RESPONSE {}", response);

        var userInfo = googleInfoUserClient.getUserInfo("json", response.getAccessToken());

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(CRole.USER_ROLE).ifPresent(roles::add);

        var user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                () -> userRepository.save(User.builder()
                                .email(userInfo.getEmail())
                                .name(userInfo.getGivenName() + " " + userInfo.getFamilyName())
                                .roles(roles)
                            .build()));

        var token = generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .build();
    }

    private SignedJWT verifyToken(String token,boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiresAt = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (isRefresh) {
            expiresAt = new Date(
                    signedJWT.getJWTClaimsSet().getIssueTime()
                            .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                            .toEpochMilli());
        }

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiresAt != null && expiresAt.after(new Date())))
            throw new IdentityException(ErrorCode.UNAUTHENTICATED);

        if(!userRepository.existsByEmail(signedJWT.getJWTClaimsSet().getSubject())) {
            log.error("user not exists");
            throw new IdentityException(ErrorCode.UNAUTHENTICATED);
        }

        if(logoutTokenRepository.existsByTokenId(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new IdentityException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("identityService")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can't create token");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }
}
