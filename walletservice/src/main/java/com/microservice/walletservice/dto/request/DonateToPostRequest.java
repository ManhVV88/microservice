package com.microservice.walletservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DonateToPostRequest {
    String postId;
    String itemType;
    Long quantity;
}
