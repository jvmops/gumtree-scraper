package com.jvmops.gumtree.scrapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("scheduler")
@Configuration
@Slf4j
public class ScrapJobConfig {
    @Autowired
    private Scheduler scheduler;

    @Bean
    JobDetail gumtreeScrapper() throws SchedulerException {
        log.info("Initializing scrapping job");
        JobDetail scrapper = JobBuilder.newJob().ofType(ScrapJob.class)
                .storeDurably()
                .withIdentity("gumtree-scrapper")
                .withDescription("Scrap ads from gumtree")
                .build();
        scheduler.addJob(scrapper, true);
        log.info("Scrapping job added to the scheduler");
        return scrapper;
    }

    @PostConstruct
    void log() {
        log.warn("BEAN: Lazy ScrapJobConfig created!");
    }

    @Getter
    @Setter
    @Slf4j
    static class ScrapJob implements Job {
        @Autowired
        private AdScrapper adScrapper;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            adScrapper.scrapAds();
            // TODO: implement
            log.error("WIP: ads are only scrapped for now");
        }
    }
}
