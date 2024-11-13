package com.microservice.walletservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class DonateHistory extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String itemType;
    Long quantity;
    String postId;
    String donateToPostUserId;
    String userId;
    Long previousQuantityPostUser;
    Long previousUserDonate;
    Long currentQuantityPostUser;
    Long currentUserDonate;
}
