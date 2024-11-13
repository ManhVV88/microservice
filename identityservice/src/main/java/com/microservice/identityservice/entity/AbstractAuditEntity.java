package com.microservice.identityservice.entity;

import com.microservice.identityservice.listener.CustomAuditingEntityListener;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(CustomAuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractAuditEntity {
    @CreationTimestamp
    ZonedDateTime createdAt;

    @CreatedBy
    String createdBy;

    @UpdateTimestamp
    ZonedDateTime updatedAt;

    @LastModifiedBy
    String updatedBy;
}
