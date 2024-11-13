package com.microservice.paymentservice.mapper;

import com.microservice.paymentservice.dto.request.PaymentSuccessRequest;
import com.microservice.paymentservice.dto.request.UpdateStatusRechargeRequest;
import com.microservice.paymentservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring" , nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {
    Payment toPayment(PaymentSuccessRequest paymentSuccessRequest);
    @Mapping(target = "status", source = "paymentStatus")
    UpdateStatusRechargeRequest toUpdateStatusRechargeRequest(PaymentSuccessRequest paymentSuccessRequest);
}
