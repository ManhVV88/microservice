package com.microservice.identityservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.identityservice.constant.EntityValidated;
import com.microservice.identityservice.constant.Existed;
import com.microservice.identityservice.validator.IsExistedEntityIdConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRequest {
    @NotBlank(message = "INVALID_BLANK")
    @Size(min = 5 , max = 36,message = "INVALID_SIZE")
    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$",message = "INVALID_EMAIL_FORMAT")
    @IsExistedEntityIdConstraint(entityValidated = EntityValidated.USER
            ,isExisted= Existed.NOT_EXISTED_IS_PASSED
            ,message = "INVALID_EMAIL_EXISTED")
    String email;

    @NotBlank(message = "INVALID_BLANK")
    @Size(max = 36,message = "INVALID_MAX")
    String password;

    @JsonFormat(pattern="yyyy-MM-dd",shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dateOfBirth;
    String name;
    String address;
}
