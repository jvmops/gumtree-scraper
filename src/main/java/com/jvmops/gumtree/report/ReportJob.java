package com.jvmops.gumtree.report;

import com.jvmops.gumtree.config.GumtreeScrapperProperties;
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
    private GumtreeScrapperProperties properties;

    @PostConstruct
    void execute() {
        notifyAboutNewReport();
    }

    private void notifyAboutNewReport() {
        properties.getCitiesToWatch().stream()
                .peek(city -> log.info("Creating an apartment report for {}", city))
                .map(apartmentReportFactory::create)
                .peek(report -> log.info("Sending {} apartment report to {}", report.getCity(), properties.getEmailAddressesBy(report.getCity())))
                .forEach(notificationSender::send);
    }
}