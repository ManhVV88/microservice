package com.microservice.walletservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.walletservice.dto.request.CreateRechargeRequest;
import com.microservice.walletservice.dto.request.UpdateStatusRechargeRequest;
import com.microservice.walletservice.dto.response.CreateRechargeResponse;
import com.microservice.walletservice.dto.response.GetRechargeIdResponse;
import com.microservice.walletservice.service.RechargePointService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/recharge_point")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RechargePointItemController {

    RechargePointService rechargePointService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateRechargeResponse>> createRecharge(@RequestBody CreateRechargeRequest createRechargeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CreateRechargeResponse>builder()
                        .result(rechargePointService.createRechargePoint(createRechargeRequest))
                        .build());
    }

    @GetMapping("/{id}")
    public ApiResponse<GetRechargeIdResponse> getRechargeId(@PathVariable String id) {

        return ApiResponse.<GetRechargeIdResponse>builder()
                .result(rechargePointService.getRechargeIdHistory(id))
                .build();
    }

    @PutMapping
    public ApiResponse<String> updateStatusRecharge(@RequestBody @Valid UpdateStatusRechargeRequest request) {
        return ApiResponse.<String>builder()
                .result(rechargePointService.updateStatusRecharge(request))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<GetRechargeIdResponse>> getAllRechargeIdByEmail() {
        return ApiResponse.<List<GetRechargeIdResponse>>builder()
                .result(rechargePointService.getRechargeIdByEmailAndStatusPending(SecurityContextHolder.getContext().getAuthentication().getName()))
                .build();
    }

}
