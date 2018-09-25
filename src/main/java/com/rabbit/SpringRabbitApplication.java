package com.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRabbitApplication {
	public static void main(String[] args) {
		System.out.println("Starting spring rabbit topic exchange application....");
		System.out.println("Topic queue 2 is excluded because we are sending message via ");
		SpringApplication.run(SpringRabbitApplication.class, args);
	}
}
