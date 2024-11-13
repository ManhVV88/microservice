package com.microservice.walletservice.repository;

import com.microservice.walletservice.entity.DetailWalletUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailWalletUserRepository extends JpaRepository<DetailWalletUser, String> {

}
