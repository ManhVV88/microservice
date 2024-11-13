package com.microservice.identityservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginRequest {
    @NotBlank(message = "INVALID_BLANK")
    @Size(min = 6, max = 36,message = "INVALID_SIZE")
    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$",message = "INVALID_EMAIL_FORMAT")
    String email;

    @NotBlank(message = "INVALID_BLANK")
    @Size(max = 36,message = "INVALID_MAX")
    String password;
}
