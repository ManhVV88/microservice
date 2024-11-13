package com.microservice.fileservice.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableMongoRepositories("com.microservice.fileservice.repository")
@EntityScan("com.microservice.fileservice.entity")
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class DatabaseConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null) {
                return Optional.of("");
            }
            return Optional.of(auth.getName());
        };
    }

    @Bean
    public AuditingHandler auditingHandler(MongoMappingContext mongoMappingContext) {
        return new AuditingHandler(PersistentEntities.of(mongoMappingContext));
    }
}
