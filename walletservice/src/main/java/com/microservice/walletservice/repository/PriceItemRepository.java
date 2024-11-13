package com.microservice.walletservice.repository;

import com.microservice.walletservice.entity.PriceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceItemRepository extends JpaRepository<PriceItem, String> {
    boolean existsByWalletItemType(String itemType);
    Optional<PriceItem> findByWalletItemType(String itemType);
    Void deleteByWalletItemType(String itemType);
}
