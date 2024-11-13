package com.microservice.fileservice.entity;



import com.microservice.fileservice.listener.CustomAuditingEntityListener;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(CustomAuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractAuditEntity {
    @CreatedDate
    Instant createdAt;

    @CreatedBy
    String createdBy;

    @LastModifiedDate
    Instant updatedAt;

    @LastModifiedBy
    String updatedBy;
}
