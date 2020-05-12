package com.jvmops.gumtree.notifications.adapters;

import com.jvmops.gumtree.notifications.NotificationService;
import com.jvmops.gumtree.notifications.model.ApartmentReportType;
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
    // @Scheduled(cron = "0 0 8,10,12,14,16,18 * * ?")
    void newestApartments() {
        log.info("Notifying subscribers with NEWEST apartments");
        notificationService.notifySubscribers(ApartmentReportType.NEWEST);
    }

    // every day at 7pm
    @Scheduled(cron = "0 0 19 * * ?")
    void dailyReport() {
        log.info("Notifying subscribers with DAILY apartments");
        notificationService.notifySubscribers(ApartmentReportType.DAILY);
    }
}
