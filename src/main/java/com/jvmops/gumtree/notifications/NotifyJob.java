package com.jvmops.gumtree.notifications;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("report")
@Component
@AllArgsConstructor
public class NotifyJob {
    private NotificationService notificationService;

    @Scheduled
    void newestApartments() {
        notificationService.notifySubscribers(ReportType.NEWEST);
    }

    @Scheduled
    void dailyReport() {
        notificationService.notifySubscribers(ReportType.DAILY);
    }
}
