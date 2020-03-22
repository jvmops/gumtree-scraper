package com.jvmops.gumtree.report;

import com.jvmops.gumtree.city.City;
import com.jvmops.gumtree.city.CityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Lazy
@Slf4j
@AllArgsConstructor
public class ReportService {
    private ApartmentReportFactory apartmentReportFactory;
    private NotificationSender notificationSender;
    private CityService cityService;

    public void createReportAndNotifySingleEmail(String cityName, String email) {
        City city = new City(cityName);
        ApartmentReport apartmentReport = apartmentReportFactory.create(city);
        notificationSender.send(apartmentReport, Set.of(email));
    }

    @SuppressWarnings("squid:S3864")
    public void createReportAndNotifyForEachCity() {
        cityService.cities().stream()
                .peek(city -> log.info("Creating {} apartment report", city.getName()))
                .map(apartmentReportFactory::create)
                .peek(report -> log.info("Sending {} apartment report to {}", report.getCity().getName(), report.getCity().getNotifications()))
                .forEach(notificationSender::send);
    }
}