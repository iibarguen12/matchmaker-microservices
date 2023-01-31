package com.contextlab.scoringservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ScoringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScoringServiceApplication.class, args);
	}

}
