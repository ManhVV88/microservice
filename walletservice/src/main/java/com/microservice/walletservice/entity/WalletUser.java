package com.microservice.walletservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true , exclude = "listDetailWallet")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class WalletUser extends AbstractAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true)
    String userId;

    @OneToMany(mappedBy = "walletUser", fetch = FetchType.EAGER , cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default()
    Set<DetailWalletUser> listDetailWallet = new HashSet<>();

    @Override
    public String toString() {
        return STR."WalletUser{id='\{id}\{'\''}, userId='\{userId}\{'\''}\{'}'}";
    }

}
