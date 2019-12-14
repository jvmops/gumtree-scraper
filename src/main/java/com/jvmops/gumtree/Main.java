package com.jvmops.gumtree;

import com.jvmops.gumtree.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class Main {
	@SuppressWarnings("squid:S4823")
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
