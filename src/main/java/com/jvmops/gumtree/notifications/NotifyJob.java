package com.jvmops.gumtree.notifications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("report")
@Component
@Slf4j
@AllArgsConstructor
public class NotifyJob {
    private NotificationService notificationService;

    // every two hour starting at 8am and ending at 8pm
    @Scheduled(cron = "0 0 8,10,12,14,16,18 20 * *")
    void newestApartments() {
        log.info("Notifing subscribers about NEWEST apartments");
        notificationService.notifySubscribers(ReportType.NEWEST);
    }

    // every day at 7pm
    @Scheduled(cron = "0 0 19 * * ?")
    void dailyReport() {
        log.info("Notifing subscribers with daily report");
        notificationService.notifySubscribers(ReportType.DAILY);
    }
}
