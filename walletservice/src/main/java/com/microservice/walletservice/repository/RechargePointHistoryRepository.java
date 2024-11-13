package com.microservice.walletservice.repository;

import com.microservice.walletservice.entity.RechargePointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RechargePointHistoryRepository extends JpaRepository<RechargePointHistory, String> {
    Optional<RechargePointHistory> findByIdAndStatusAndEmail(String rechargePointId, String status,String email);
    List<RechargePointHistory> findByStatusAndEmail(String status, String email);
}
