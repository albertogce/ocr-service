package com.docstream.ocrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.docstream.ocrservice",
		"com.docstream.commondata"
})
public class OcrServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OcrServiceApplication.class, args);
	}

}
