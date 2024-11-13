package com.microservice.walletservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String id;
    String title;
    String content;
    String authorEmail;
    List<Image> listImages;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
}
