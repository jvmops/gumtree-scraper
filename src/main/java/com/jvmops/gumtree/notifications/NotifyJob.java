package com.jvmops.gumtree.notifications;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
class NotifyJob {
    private ApartmentReportFactory apartmentReportFactory;
    private NotificationSender notificationSender;

    void execute() {
        notifyAboutNewReport();
    }

    private void notifyAboutNewReport() {
        log.info("Job executed. Creating an apartment report!");
        var apartmentReport = apartmentReportFactory.create();

        log.info("Apartment report created. Notifying watchers!");
        notificationSender.send(
                apartmentReport.getReport(),
                apartmentReport.getContentType()
        );
    }
}