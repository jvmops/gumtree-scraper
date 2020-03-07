package com.jvmops.gumtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
	// sonar stuff
	@SuppressWarnings("squid:S4823")
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
