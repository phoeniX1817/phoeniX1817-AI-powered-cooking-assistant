package com.smartrecipe.cooking_assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.smartrecipe.cooking_assistant")
public class CookingAssistantApplication {
	public static void main(String[] args) {
		SpringApplication.run(CookingAssistantApplication.class, args);
	}
}
