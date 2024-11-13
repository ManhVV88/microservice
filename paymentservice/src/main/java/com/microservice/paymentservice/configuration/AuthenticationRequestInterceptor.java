package com.microservice.paymentservice.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
public class AuthenticationRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if(requestAttributes != null) {
            var authHeader = requestAttributes.getRequest().getHeader("Authorization");
            log.info("Header: {}", authHeader);
            if (StringUtils.hasText(authHeader))
                requestTemplate.header("Authorization", authHeader);
        } else {
            requestTemplate.header("Authorization", "");
        }
    }
}
