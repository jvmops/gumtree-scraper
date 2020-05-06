package com.jvmops.gumtree.notifications;

import com.jvmops.gumtree.subscriptions.City;
import com.jvmops.gumtree.subscriptions.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
public class NotificationService {
    private ApartmentReportFactory apartmentReportFactory;
    private NotificationSender notificationSender;
    private CityService cityService;

    public void initialEmail(City city, String email) {
        ApartmentReport apartmentReport = apartmentReportFactory.create(city);
        notificationSender.initialEmail(apartmentReport, email);
    }

    @SuppressWarnings("squid:S3864")
    public void notifySubscribers() {
        cityService.cities().stream()
                .peek(city -> log.info("Creating {} apartment report", city.getName()))
                .map(apartmentReportFactory::create)
                .peek(report -> log.info("Preparing to notify {} subscribers about new apartment report", report.getCity().getName()))
                .forEach(notificationSender::notifySubscribers);
    }
}