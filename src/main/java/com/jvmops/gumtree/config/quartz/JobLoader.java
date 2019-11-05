package com.jvmops.gumtree.config.quartz;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Component
@AllArgsConstructor
@Slf4j
@Profile("scheduler")
public class JobLoader {

    private Scheduler scheduler;
    private JobDetail gumtreeScrapper;
    private JobDetail reportSender;


    @PostConstruct
    void setup() {
        log.info("Scheduling jobs...");
        schedulJob(scrapTrigger());
        schedulJob(notifyTrigger());
    }

    private Trigger scrapTrigger() {
        return TriggerBuilder.newTrigger().forJob(gumtreeScrapper)
                .withIdentity("gumtree-scrapper")
                .withSchedule(cronSchedule("0 0 */1 ? * *"))
                .build();
    }

    private Trigger notifyTrigger() {
        return TriggerBuilder.newTrigger().forJob(reportSender)
                .withIdentity("gumtree-report-sender")
                .withSchedule(cronSchedule("0 0 12,21 ? * *"))
                .build();
    }

    private void schedulJob(Trigger trigger) {
        try {
            scheduler.scheduleJob(trigger);
        } catch (SchedulerException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
