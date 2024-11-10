package com.challenge.operations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The OperationsApplication class serves as the entry point for the Spring Boot application.
 *
 * This is a standard bootstrap class for a Spring Boot application, which uses the
 * @SpringBootApplication annotation to enable auto-configuration, component scanning,
 * and other Spring Boot features.
 *
 * The main method initializes and runs the entire Spring Boot application by invoking
 * SpringApplication.run with the current class and the provided arguments.
 */
@SpringBootApplication
public class OperationsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperationsApplication.class, args);
	}

}
