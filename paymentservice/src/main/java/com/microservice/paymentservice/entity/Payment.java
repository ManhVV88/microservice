package com.microservice.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Payment extends AbstractAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String rechargeId;
    BigDecimal amount;
    BigDecimal paymentFee;
    String gatewayTransactionId;
    String paymentMethod;
    String paymentStatus;
    String failureMessage;
}
