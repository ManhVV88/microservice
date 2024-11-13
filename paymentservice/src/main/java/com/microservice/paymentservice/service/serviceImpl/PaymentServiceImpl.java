package com.microservice.paymentservice.service.serviceImpl;

import com.microservice.paymentservice.dto.request.PaymentSuccessRequest;
import com.microservice.paymentservice.dto.request.WalletUpdateRequest;
import com.microservice.paymentservice.dto.response.WalletResponse;
import com.microservice.paymentservice.exception.ErrorCode;
import com.microservice.paymentservice.exception.PaymentException;
import com.microservice.paymentservice.mapper.PaymentMapper;
import com.microservice.paymentservice.repository.PaymentRepository;
import com.microservice.paymentservice.repository.httpClient.WalletClient;
import com.microservice.paymentservice.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    WalletClient walletClient;

    @Override
    public WalletResponse paymentSuccess(PaymentSuccessRequest paymentSuccessRequest) {
        paymentRepository.save(paymentMapper.toPayment(paymentSuccessRequest));
        var paymentRequest = paymentMapper.toUpdateStatusRechargeRequest(paymentSuccessRequest);
        var rechargeId = walletClient.updateStatusRecharge(paymentRequest);
        if(rechargeId.getResult().equals("null")) {
            log.warn("Payment update recharge status history failed : {}", paymentRequest.getRechargeId());
        }
        var walletRequest = WalletUpdateRequest.builder()
                .quantity(paymentSuccessRequest.getQuantity())
                .itemType(paymentSuccessRequest.getItemType())
                .build();
        var walletUser = walletClient.updateWalletUser(walletRequest).getResult();

        if(walletUser == null) {
            log.error("Payment recharge failed walletUser is : null");
            throw new PaymentException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        return WalletResponse.builder()
                .email(SecurityContextHolder.getContext().getAuthentication().getName())
                .ItemType(paymentSuccessRequest.getItemType())
                .priviousQuantity(walletUser.getBalanceResponse().getPreviousBalance())
                .currentQuantity(walletUser.getBalanceResponse().getCurrentBalance())
                .build();
    }
}
