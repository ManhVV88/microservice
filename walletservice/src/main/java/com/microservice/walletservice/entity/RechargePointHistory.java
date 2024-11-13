package com.microservice.walletservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class RechargePointHistory extends  AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String transactionId;
    Double amount;
    String currency;
    String status;
    Instant transactionDate;
    String referenceId;
    String userId;
    String email;
    String paymentMethod;
    String walletItemType;
    Long quantity;
}
