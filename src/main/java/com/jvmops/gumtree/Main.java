package com.jvmops.gumtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories
@EnableConfigurationProperties(ScrapperProperties.class)
public class Main {
	// sonar stuff
	@SuppressWarnings("squid:S4823")
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
