package com.microservice.postservice.listener;

import com.microservice.postservice.entity.AbstractAuditEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal=true)
@RequiredArgsConstructor
public class CustomAuditingEntityListener extends AbstractMongoEventListener<Object> {
    AuditingHandler auditingHandler;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object target = event.getSource();
        if (target instanceof AbstractAuditEntity entity) {
            // Xử lý tương tự cho CreatedBy
            if (entity.getCreatedBy() == null) {
                auditingHandler.markCreated(target);
            }

            // Set UpdatedBy nếu chưa có
            if (entity.getUpdatedBy() == null && entity.getCreatedBy() != null) {
                entity.setUpdatedBy(entity.getCreatedBy());
            }
        }
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        Object target = event.getSource();
        if (target instanceof AbstractAuditEntity entity) {
            // Xử lý tương tự cho UpdatedBy
            if (entity.getUpdatedBy() == null) {
                auditingHandler.markModified(target);
            }
        }
    }
}
