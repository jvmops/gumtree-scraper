package com.jvmops.gumtree.report;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("report")
@Component
@Slf4j
@AllArgsConstructor
public class ReportJob {
    private ApartmentReportFactory apartmentReportFactory;
    private NotificationSender notificationSender;

    @PostConstruct
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