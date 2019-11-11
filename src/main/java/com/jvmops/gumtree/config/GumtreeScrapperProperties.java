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
    private final List<String> emailAddresses;

    @ConstructorBinding
    public GumtreeScrapperProperties(String[] emailAddresses) {
        this.emailAddresses = Arrays.stream(emailAddresses)
                .map(String::trim)
                .collect(Collectors.toUnmodifiableList());
    }
}
