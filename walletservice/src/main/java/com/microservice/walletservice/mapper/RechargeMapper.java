package com.microservice.walletservice.mapper;

import com.microservice.walletservice.dto.response.CreateRechargeResponse;
import com.microservice.walletservice.dto.response.GetRechargeIdResponse;
import com.microservice.walletservice.entity.RechargePointHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RechargeMapper {
    CreateRechargeResponse toRechargeResponse(RechargePointHistory rechargePointHistory);

    @Mapping(target = "rechargeHistoryId", source = "id")
    GetRechargeIdResponse toGetRechargeIdResponse(RechargePointHistory rechargePointHistory);
}
