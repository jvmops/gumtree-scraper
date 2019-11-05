package com.jvmops.gumtree.notifications;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Configuration
@Profile("scheduler")
@Slf4j
class NotifyJobConfig {
    @Autowired
    private Scheduler scheduler;

    @Bean
    JobDetail reportSender() throws SchedulerException {
        log.info("Initializing report sending job");
        JobDetail notify = JobBuilder.newJob().ofType(NotifyJob.class)
                .storeDurably()
                .withIdentity("gumtree-report-sender")
                .withDescription("Sends apartment report")
                .build();
        scheduler.addJob(notify, true);
        log.info("gumtree-report-sender added to the scheduler");
        return notify;
    }


    @PostConstruct
    void log() {
        log.info("JOB: NotifyJobConfig created!");
    }

    @Getter
    @Setter
    class NotifyJob implements Job {
        @Autowired
        private ApartmentReportFactory apartmentReportFactory;
        @Autowired
        private NotificationSender notificationSender;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            notifyAboutNewReport();
        }

        private void notifyAboutNewReport() {
            var apartmentReport = apartmentReportFactory.create();
            notificationSender.send(
                    apartmentReport.getReport(),
                    apartmentReport.getContentType()
            );
        }
    }
}
