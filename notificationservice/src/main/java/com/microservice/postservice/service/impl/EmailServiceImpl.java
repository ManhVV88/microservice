package com.microservice.postservice.service.impl;

import com.microservice.postservice.dto.request.EmailRequest;
import com.microservice.postservice.dto.request.SendEmailRequest;
import com.microservice.postservice.dto.request.Sender;
import com.microservice.postservice.dto.response.EmailResponse;
import com.microservice.postservice.exception.ErrorCode;
import com.microservice.postservice.exception.NotificationException;
import com.microservice.postservice.repository.httpclient.EmailClient;
import com.microservice.postservice.service.EmailService;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class EmailServiceImpl implements EmailService {
    EmailClient emailClient;


    @Value("${app.api.brevo.key}")
    @NonFinal
    String apiKey;


    @Override
    public EmailResponse sendEmail(SendEmailRequest request) {
        try{
            return emailClient.sendEmail(apiKey, EmailRequest.builder()
                    .sender(Sender.builder()
                            .name("Vu Manh")
                            .email("vanmanhvn@gmail.com")
                            .build())
                    .to(List.of(request.getTo()))
                    .htmlContent(request.getHtmlContent())
                    .subject(request.getSubject())
                    .build());
        } catch (FeignException e ) {
            e.printStackTrace();
            log.error("Email can't send : {}", e.getMessage());
            throw new NotificationException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
