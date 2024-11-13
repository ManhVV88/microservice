package com.microservice.identityservice.repository.httpClient;

import com.microservice.identityservice.dto.request.GoogleTokenRequest;
import com.microservice.identityservice.dto.response.GoogleTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-identity",url = "https://oauth2.googleapis.com")
public interface GoogleIdenityClient {
    @PostMapping(value = "/token", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse getGoogleToken(@QueryMap GoogleTokenRequest googleTokenRequest);
}
