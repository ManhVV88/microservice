package com.microservice.walletservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true,exclude = "walletUser")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class DetailWalletUser extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String itemType;
    Long quantity;

    @Version
    Long version;

    @ManyToOne
    @JoinColumn(name="wallet_user_id")
    WalletUser walletUser;
}
