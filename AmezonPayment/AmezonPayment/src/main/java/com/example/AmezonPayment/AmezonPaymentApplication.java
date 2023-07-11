package com.example.AmezonPayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AmezonPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmezonPaymentApplication.class, args);
		System.out.println("This is Amezon  Payment Service running on 8181");
	}

}
