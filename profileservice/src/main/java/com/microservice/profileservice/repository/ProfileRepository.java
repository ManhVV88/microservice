package com.microservice.profileservice.repository;

import com.microservice.profileservice.entity.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends Neo4jRepository<UserProfile, String> {
    List<UserProfile> findByEmail(String email);
}
