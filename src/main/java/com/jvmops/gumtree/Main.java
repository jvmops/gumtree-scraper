package com.jvmops.gumtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class Main {
	@SuppressWarnings("squid:S4823")
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
