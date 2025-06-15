package com.docstream.parserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.docstream.parserservice",
		"com.docstream.commondata"
})
public class ParserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParserServiceApplication.class, args);
	}

}
