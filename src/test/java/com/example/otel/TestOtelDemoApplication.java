package com.example.otel;

import org.springframework.boot.SpringApplication;

public class TestOtelDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(OtelDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
