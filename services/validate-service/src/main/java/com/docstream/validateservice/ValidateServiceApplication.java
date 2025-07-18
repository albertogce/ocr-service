package com.docstream.validateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.docstream.validateservice",
		"com.docstream.commondata"
})
public class ValidateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidateServiceApplication.class, args);
	}

}
