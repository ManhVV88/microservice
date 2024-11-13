package com.microservice.identityservice.repository;

import com.microservice.identityservice.entity.LogoutToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogoutTokenRepository extends JpaRepository<LogoutToken, String> {
    boolean existsByTokenId(String tokenId);
}
