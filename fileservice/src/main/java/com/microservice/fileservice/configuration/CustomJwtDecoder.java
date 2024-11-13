package com.microservice.fileservice.configuration;

import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    @NonFinal
    NimbusJwtDecoder nimbusJwtDecoder = null;


    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
                SignedJWT signedJWT = SignedJWT.parse(token);

                return new Jwt(token,
                        signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                        signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                        signedJWT.getHeader().toJSONObject(),
                        signedJWT.getJWTClaimsSet().getClaims()
                );


        } catch (ParseException e) {
            log.error("Can't parse token value : {}", token);
            token = "Invalid JWT";
            if(Objects.isNull(nimbusJwtDecoder)) {
                SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS512)
                        .build();
            }

            return nimbusJwtDecoder.decode(token);
        }

    }
}
