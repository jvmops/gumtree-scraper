package com.jvmops.gumtree;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@ConfigurationProperties(prefix = "gumtree.scraper")
@Getter
@Setter
@Slf4j
public class ScrapperProperties {
    private int maxScrappedPages = 2;
    private String websiteUrl = "http://localhost:8080";

    @PostConstruct
    void log() {
        log.info("Limit of pages to scrap: {}", maxScrappedPages);
        log.info("Website url: {}", websiteUrl);
    }
}
