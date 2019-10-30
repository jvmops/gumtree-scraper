package com.jvmops.gumtree.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "gumtree.scrapper")
@Getter
public class GumtreeScrapperProperties {
    private final String seleniumUrl;
    private final List<String> notificationEmails;

    @ConstructorBinding
    public GumtreeScrapperProperties(String seleniumUrl, String[] notificationEmails) {
        this.seleniumUrl = seleniumUrl;
        this.notificationEmails = Arrays.stream(notificationEmails)
                .map(String::trim)
                .collect(Collectors.toUnmodifiableList());
    }
}
