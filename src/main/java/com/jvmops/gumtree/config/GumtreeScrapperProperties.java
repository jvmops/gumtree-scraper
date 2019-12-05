package com.jvmops.gumtree.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;
import java.util.Set;

@ConfigurationProperties(prefix = "gumtree.scrapper")
@Getter
public class GumtreeScrapperProperties {
    private final Map<String, String> cities;

    @ConstructorBinding
    public GumtreeScrapperProperties(Map<String, String> cities) {
        this.cities = cities;
    }

    public Set<String> getCitiesToWatch() {
        return cities.keySet();
    }

    public Set<String> getEmailAddressesBy(String city) {
        String[] emails = cities.get(city)
                .split(",");
        return Set.of(emails);
    }
}
