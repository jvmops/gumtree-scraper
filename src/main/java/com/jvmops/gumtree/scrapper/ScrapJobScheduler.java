package com.jvmops.gumtree.scrapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("scheduler")
@Component
@Slf4j
@AllArgsConstructor
public class ScrapJobScheduler {
    private ScrapJob scrapJob;

    @Bean
    public void scrapAds() {
        scrapJob.execute();
    }

    @PostConstruct
    void log() {
        log.warn("BEAN: ScrapJobScheduler initialized!");
    }
}
