package com.example.otel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class OtelDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtelDemoApplication.class, args);
	}

}
