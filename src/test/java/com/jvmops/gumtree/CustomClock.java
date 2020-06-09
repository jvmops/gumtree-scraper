package com.jvmops.gumtree;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@Configuration
public class CustomClock {
    @Bean
    @Primary
    public Clock fixedClock() {
        return staticlyFixedClock();
    }

    public static Clock staticlyFixedClock() {
        Instant instant = Instant.parse("2020-05-07T21:01:00Z");
        return Clock.fixed(instant, ZoneId.of("+2"));
    }
}
