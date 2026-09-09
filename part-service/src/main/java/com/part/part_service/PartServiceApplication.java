package com.part.part_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartServiceApplication.class, args);
	}

}
