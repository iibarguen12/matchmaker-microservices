package com.contextlab.rulesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RulesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RulesServiceApplication.class, args);
	}

}
