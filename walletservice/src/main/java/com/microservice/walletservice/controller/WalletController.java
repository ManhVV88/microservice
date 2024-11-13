package com.microservice.walletservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.walletservice.dto.request.DonateToPostRequest;
import com.microservice.walletservice.dto.request.WalletUpdateRequest;
import com.microservice.walletservice.dto.response.WalletUserResponse;
import com.microservice.walletservice.repository.WalletUserRepository;
import com.microservice.walletservice.repository.httpClient.IdentityClient;
import com.microservice.walletservice.service.WalletUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class WalletController {

    WalletUserRepository walletUserRepository;
    IdentityClient identityClient;
    WalletUserService walletUserService;

    @GetMapping("/info")
    public ApiResponse<WalletUserResponse> getWalletUser() {
        return ApiResponse.<WalletUserResponse>builder()
                .result(walletUserService.getWalletUser())
                .build();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WalletUserResponse>> updateWalletUser(@RequestBody WalletUpdateRequest request) {

        return ResponseEntity.status(
                walletUserRepository.existsByUserId(identityClient.getUserId("")
                        .getResult().get(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId())
                        ? HttpStatus.OK : HttpStatus.CREATED)
                .body(ApiResponse.<WalletUserResponse>builder()
                        .result(walletUserService.updateWalletUser(request))
                        .build());
    }
    @PostMapping("/donate")
    public ApiResponse<String> donateToPostUser(@RequestBody DonateToPostRequest request){
        return ApiResponse.<String>builder()
                .result(walletUserService.donateToPostUser(request))
                .build();
    }
}
