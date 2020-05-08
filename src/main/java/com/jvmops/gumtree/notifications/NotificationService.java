package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.subscriptions.City;
import com.jvmops.gumtree.subscriptions.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
public class NotificationService {
    private ApartmentReportFactory apartmentReportFactory;
    private NotificationSender notificationSender;
    private CityService cityService;

    public void initialEmail(City city, String email) {
        ApartmentReport apartmentReport = apartmentReportFactory.create(city, ReportType.INITIAL);
        notificationSender.initialEmail(apartmentReport, email);
    }

    @SuppressWarnings("squid:S3864")
    public void notifySubscribers(ReportType reportType) {
        cityService.cities().stream()
                .peek(city -> log.info("Creating {} apartment report", city.getName()))
                .map(city -> apartmentReportFactory.create(city, reportType))
                .filter(Predicate.not(ApartmentReport::isEmpty))
                .peek(report -> log.info("Preparing to notify {} subscribers about new {} apartment report", report.getCity().getName(), reportType))
                .forEach(notificationSender::notifySubscribers);
    }
}