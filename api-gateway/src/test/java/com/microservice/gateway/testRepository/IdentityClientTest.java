package com.microservice.gateway.testRepository;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.gateway.dto.request.ValidateTokenRequest;
import com.microservice.gateway.dto.response.ValidateTokenResponse;
import com.microservice.gateway.repository.IdentityClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;


@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class IdentityClientTest {

    @Autowired
    private IdentityClient identityClient;

    @Test
    public void testLogin() {
        ValidateTokenRequest request = new ValidateTokenRequest("some-token");
        Mono<ApiResponse<ValidateTokenResponse>> response = identityClient.login(request);

        // Verify response...
        response.subscribe(apiResponse -> {
            if(apiResponse.getResult().isValidToken())
                log.info("can't validate token : {} " , apiResponse.getResult().isValidToken());
            else
                log.error("can't validate token : {} " , apiResponse.getResult().isValidToken());
            // Other assertions...
        });
    }
}