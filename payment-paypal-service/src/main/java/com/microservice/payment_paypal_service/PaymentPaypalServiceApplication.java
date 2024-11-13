package com.microservice.payment_paypal_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PaymentPaypalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentPaypalServiceApplication.class, args);
	}

}
