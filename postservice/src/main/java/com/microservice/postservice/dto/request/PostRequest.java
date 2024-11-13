package com.microservice.postservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {
    String title;
    String content;
    @NotNull(message = "IMAGES_URL_NOT_NULL")
    List<String> urlImages;
}
