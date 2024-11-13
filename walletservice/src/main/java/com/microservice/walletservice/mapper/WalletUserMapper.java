package com.microservice.walletservice.mapper;

import com.microservice.walletservice.dto.response.DetailWalletUserResponse;
import com.microservice.walletservice.dto.response.WalletUserResponse;
import com.microservice.walletservice.entity.DetailWalletUser;
import com.microservice.walletservice.entity.WalletUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface WalletUserMapper {
    Set<DetailWalletUserResponse> toSetDetailResponse(Set<DetailWalletUser> detailWalletUsers);
    @Mapping(target = "listDetail", source = "listDetailWallet")
    WalletUserResponse toWalletUserResponse(WalletUser walletUser);
}
