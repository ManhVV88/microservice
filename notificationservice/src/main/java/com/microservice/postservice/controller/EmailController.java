package com.microservice.postservice.controller;

import com.microservice.commonbase.service.dto.ApiResponse;
import com.microservice.postservice.dto.request.SendEmailRequest;
import com.microservice.postservice.dto.response.EmailResponse;
import com.microservice.postservice.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;

    @GetMapping
    ApiResponse<String> helloWorld(){
        return ApiResponse.<String>builder().message("Hello World").build();
    }

    @PostMapping("/email/send")
    ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest sendEmailRequest){
        return ApiResponse.<EmailResponse>builder()
                .result(emailService.sendEmail(sendEmailRequest))
                .build();
    }
}
