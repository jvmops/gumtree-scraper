package com.jvmops.gumtree.report;

import com.jvmops.gumtree.config.ManagedConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
public class ReportService {
    private ApartmentReportFactory apartmentReportFactory;
    private NotificationSender notificationSender;
    private ManagedConfiguration config;

    public void createReportAndNotifySingleEmail(String city, String email) {
        ApartmentReport apartmentReport = apartmentReportFactory.create(city);
        notificationSender.send(apartmentReport, Set.of(email));
    }

    @SuppressWarnings("squid:S3864")
    public void createReportAndNotifyForEachCity() {
        config.getCities().stream()
                .peek(city -> log.info("Creating an apartment report for {}", city))
                .map(apartmentReportFactory::create)
                .peek(report -> log.info("Sending {} apartment report to {}", report.getCity(), config.getEmails(report.getCity())))
                .forEach(notificationSender::send);
    }
}