package com.microservice.fileservice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(value = "image")
public class Image extends AbstractAuditEntity{
    @MongoId
    String id;
    String url;
    String fileType;
    String thumbnailUrl;
    Long fileSize;
    String dimensions;
}
