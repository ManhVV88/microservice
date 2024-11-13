package com.microservice.walletservice.repository;

import com.microservice.walletservice.entity.WalletUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WalletUserRepository extends JpaRepository<WalletUser, String> {
    boolean existsByUserId(String userId);
    Optional<WalletUser> findByUserId(String userId);
    List<WalletUser> findByUserIdOrUserId(String currentUserId,String donateUserId);
}
