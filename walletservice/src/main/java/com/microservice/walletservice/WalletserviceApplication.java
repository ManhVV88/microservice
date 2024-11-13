package com.microservice.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WalletserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletserviceApplication.class, args);
	}

}
