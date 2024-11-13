package com.microservice.postservice.controller;

import com.microservice.commonbase.event.dto.NotificationEvent;
import com.microservice.postservice.dto.request.Recipient;
import com.microservice.postservice.dto.request.SendEmailRequest;
import com.microservice.postservice.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    EmailService emailService;

    @KafkaListener(topics = "notification-event")
    public void listenNotificationEvent(NotificationEvent notificationEvent) {
        log.info("Received notification event: {}", notificationEvent);
        emailService.sendEmail(SendEmailRequest.builder()
                        .to(Recipient.builder()
                                .email(notificationEvent.getRecipient())
                                .build())
                        .subject(notificationEvent.getSubject())
                        .htmlContent(notificationEvent.getBody())
                .build());
    }
}
