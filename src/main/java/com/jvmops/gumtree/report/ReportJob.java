package com.jvmops.gumtree.report;

import com.jvmops.gumtree.config.ManagedConfiguration;
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
    private ManagedConfiguration config;

    @PostConstruct
    void execute() {
        notifyAboutNewReport();
    }

    private void notifyAboutNewReport() {
        config.getCities().stream()
                .peek(city -> log.info("Creating an apartment report for {}", city))
                .map(apartmentReportFactory::create)
                .peek(report -> log.info("Sending {} apartment report to {}", report.getCity(), config.getEmails(report.getCity())))
                .forEach(notificationSender::send);
    }
}