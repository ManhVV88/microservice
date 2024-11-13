package com.microservice.walletservice.service;

import com.microservice.walletservice.dto.request.CreateRechargeRequest;
import com.microservice.walletservice.dto.request.UpdateStatusRechargeRequest;
import com.microservice.walletservice.dto.response.CreateRechargeResponse;
import com.microservice.walletservice.dto.response.GetRechargeIdResponse;

import java.util.List;

public interface RechargePointService {
    CreateRechargeResponse createRechargePoint(CreateRechargeRequest checkOutRequest);
    GetRechargeIdResponse getRechargeIdHistory(String idHistory);
    String updateStatusRecharge(UpdateStatusRechargeRequest request);
    List<GetRechargeIdResponse> getRechargeIdByEmailAndStatusPending(String email);
}
