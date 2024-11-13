package com.microservice.postservice.service;

import com.microservice.postservice.dto.request.SendEmailRequest;
import com.microservice.postservice.dto.response.EmailResponse;

public interface EmailService {
    EmailResponse sendEmail(SendEmailRequest request);
}
