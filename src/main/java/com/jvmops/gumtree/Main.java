package com.jvmops.gumtree;

import com.jvmops.gumtree.config.GumtreeScrapperProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({GumtreeScrapperProperties.class})
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
