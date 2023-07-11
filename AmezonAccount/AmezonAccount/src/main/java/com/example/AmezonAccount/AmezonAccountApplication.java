package com.example.AmezonAccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AmezonAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmezonAccountApplication.class, args);
		System.out.println(" This Amezon Account running  on 9090 ");
	}

}
