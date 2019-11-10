package com.jvmops.gumtree.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

@Configuration
@Profile("scheduler")
@Slf4j
class NotifyJobScheduler {

    @Autowired
    private NotifyJob notifyJob;

    @Scheduled(cron = "0 0 12,21 ? * *")
    void reportSender() {
        notifyJob.execute();
    }

    @PostConstruct
    void log() {
        log.warn("BEAN: Lazy NotifyJobScheduler created!");
    }
}
