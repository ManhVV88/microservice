package com.microservice.walletservice.service;

import com.microservice.walletservice.dto.request.DonateToPostRequest;
import com.microservice.walletservice.dto.request.WalletUpdateRequest;
import com.microservice.walletservice.dto.response.WalletUserResponse;

public interface WalletUserService {
    WalletUserResponse getWalletUser();
    WalletUserResponse updateWalletUser(WalletUpdateRequest request);
    String donateToPostUser(DonateToPostRequest request);
}
